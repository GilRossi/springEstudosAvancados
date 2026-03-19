package estudos.spring.avancado.estudos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.AtualizarStatusRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.kafka.KafkaConsumerService;
import estudos.spring.avancado.estudos.kafka.KafkaProducerService;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import estudos.spring.avancado.estudos.rabbitmq.RabbitMQConsumerService;
import estudos.spring.avancado.estudos.rabbitmq.RabbitMQProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PedidoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private KafkaProducerService kafkaProducerService;

    @MockitoBean
    private KafkaConsumerService kafkaConsumerService;

    @MockitoBean
    private RabbitMQProducerService rabbitMQProducerService;

    @MockitoBean
    private RabbitMQConsumerService rabbitMQConsumerService;

    @Test
    void criarPedido_comDadosValidos_deveRetornar201() throws Exception {
        CriarPedidoRequest request = new CriarPedidoRequest("Notebook", 1, BigDecimal.valueOf(3000));

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.produto").value("Notebook"))
                .andExpect(jsonPath("$.quantidade").value(1))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(header().exists("Location"));
    }

    @Test
    void criarPedido_comDadosInvalidos_deveRetornar400() throws Exception {
        CriarPedidoRequest request = new CriarPedidoRequest("", -1, BigDecimal.valueOf(-10));

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos").exists());
    }

    @Test
    void buscarPorId_existente_deveRetornar200() throws Exception {
        Long id = criarPedidoERetornarId();

        mockMvc.perform(get("/pedidos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.produto").value("Mouse"));
    }

    @Test
    void buscarPorId_inexistente_deveRetornar404() throws Exception {
        mockMvc.perform(get("/pedidos/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Recurso não encontrado"));
    }

    @Test
    void listarTodos_deveRetornar200ComPaginacao() throws Exception {
        criarPedidoERetornarId();

        mockMvc.perform(get("/pedidos")
                        .param("pagina", "0")
                        .param("tamanhoPagina", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10));
    }

    @Test
    void atualizarPedido_deveRetornar200() throws Exception {
        Long id = criarPedidoERetornarId();
        AtualizarPedidoRequest request = new AtualizarPedidoRequest("Teclado Mecânico", 3, BigDecimal.valueOf(500));

        mockMvc.perform(put("/pedidos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produto").value("Teclado Mecânico"))
                .andExpect(jsonPath("$.quantidade").value(3));
    }

    @Test
    void deletarPedido_existente_deveRetornar204() throws Exception {
        Long id = criarPedidoERetornarId();

        mockMvc.perform(delete("/pedidos/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/pedidos/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarStatus_transicaoValida_deveRetornar200() throws Exception {
        Long id = criarPedidoERetornarId();
        AtualizarStatusRequest request = new AtualizarStatusRequest(StatusPedido.CANCELADO);

        mockMvc.perform(patch("/pedidos/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADO"));
    }

    @Test
    void atualizarStatus_transicaoInvalida_deveRetornar422() throws Exception {
        Long id = criarPedidoERetornarId();

        // Primeiro cancela
        AtualizarStatusRequest cancelar = new AtualizarStatusRequest(StatusPedido.CANCELADO);
        mockMvc.perform(patch("/pedidos/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelar)));

        // Tenta voltar para PROCESSANDO (transição inválida)
        AtualizarStatusRequest request = new AtualizarStatusRequest(StatusPedido.PROCESSANDO);
        mockMvc.perform(patch("/pedidos/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.erro").value("Operação inválida"));
    }

    private Long criarPedidoERetornarId() throws Exception {
        CriarPedidoRequest request = new CriarPedidoRequest("Mouse", 2, BigDecimal.valueOf(150));

        String response = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }
}
