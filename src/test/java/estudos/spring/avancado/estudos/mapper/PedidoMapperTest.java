package estudos.spring.avancado.estudos.mapper;

import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {

    private PedidoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PedidoMapper();
    }

    @Test
    void toEntity_deveMapearCamposCorretamente() {
        CriarPedidoRequest request = new CriarPedidoRequest("Notebook", 2, BigDecimal.valueOf(5000));

        Pedido pedido = mapper.toEntity(request);

        assertEquals("Notebook", pedido.getProduto());
        assertEquals(2, pedido.getQuantidade());
        assertEquals(BigDecimal.valueOf(5000), pedido.getValor());
        assertNull(pedido.getId());
    }

    @Test
    void toResponse_deveMapearTodosCampos() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .produto("Mouse")
                .quantidade(5)
                .valor(BigDecimal.valueOf(150))
                .status(StatusPedido.PENDENTE)
                .dataCriacao(LocalDateTime.of(2026, 1, 15, 10, 30))
                .build();

        PedidoResponse response = mapper.toResponse(pedido);

        assertEquals(1L, response.getId());
        assertEquals("Mouse", response.getProduto());
        assertEquals(5, response.getQuantidade());
        assertEquals(BigDecimal.valueOf(150), response.getValor());
        assertEquals("PENDENTE", response.getStatus());
        assertEquals(LocalDateTime.of(2026, 1, 15, 10, 30), response.getDataCriacao());
    }

    @Test
    void toResponseList_deveMapearListaCompleta() {
        List<Pedido> pedidos = List.of(
                Pedido.builder().id(1L).produto("A").quantidade(1).valor(BigDecimal.TEN)
                        .status(StatusPedido.PENDENTE).dataCriacao(LocalDateTime.now()).build(),
                Pedido.builder().id(2L).produto("B").quantidade(2).valor(BigDecimal.valueOf(20))
                        .status(StatusPedido.PROCESSANDO).dataCriacao(LocalDateTime.now()).build()
        );

        List<PedidoResponse> responses = mapper.toResponseList(pedidos);

        assertEquals(2, responses.size());
        assertEquals("A", responses.get(0).getProduto());
        assertEquals("B", responses.get(1).getProduto());
    }

    @Test
    void atualizarEntity_deveAtualizarApenasCamposNaoNulos() {
        Pedido pedido = Pedido.builder()
                .id(1L).produto("Original").quantidade(1).valor(BigDecimal.TEN)
                .status(StatusPedido.PENDENTE).dataCriacao(LocalDateTime.now()).build();

        AtualizarPedidoRequest request = new AtualizarPedidoRequest("Atualizado", null, null);

        mapper.atualizarEntity(request, pedido);

        assertEquals("Atualizado", pedido.getProduto());
        assertEquals(1, pedido.getQuantidade());
        assertEquals(BigDecimal.TEN, pedido.getValor());
    }

    @Test
    void atualizarEntity_deveAtualizarTodosCamposQuandoFornecidos() {
        Pedido pedido = Pedido.builder()
                .id(1L).produto("Original").quantidade(1).valor(BigDecimal.TEN)
                .status(StatusPedido.PENDENTE).dataCriacao(LocalDateTime.now()).build();

        AtualizarPedidoRequest request = new AtualizarPedidoRequest("Novo", 10, BigDecimal.valueOf(99));

        mapper.atualizarEntity(request, pedido);

        assertEquals("Novo", pedido.getProduto());
        assertEquals(10, pedido.getQuantidade());
        assertEquals(BigDecimal.valueOf(99), pedido.getValor());
    }
}
