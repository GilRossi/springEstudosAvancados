package estudos.spring.avancado.estudos.service.impl;

import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.event.PedidoStatusEventPublisher;
import estudos.spring.avancado.estudos.exception.PedidoNaoEncontradoException;
import estudos.spring.avancado.estudos.exception.TransicaoStatusInvalidaException;
import estudos.spring.avancado.estudos.kafka.KafkaProducerService;
import estudos.spring.avancado.estudos.mapper.PedidoMapper;
import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import estudos.spring.avancado.estudos.rabbitmq.RabbitMQProducerService;
import estudos.spring.avancado.estudos.repository.PedidoRepository;
import estudos.spring.avancado.estudos.service.ProcessamentoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private RabbitMQProducerService rabbitMQProducerService;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private PedidoStatusEventPublisher eventPublisher;

    @Mock
    private ProcessamentoService processamentoService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void criarPedido_deveSalvarERetornarResponse() {
        CriarPedidoRequest request = new CriarPedidoRequest("Notebook", 1, BigDecimal.valueOf(3000));
        Pedido pedido = criarPedidoBase();
        PedidoResponse expectedResponse = criarResponseBase();

        when(pedidoMapper.toEntity(request)).thenReturn(pedido);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);
        when(pedidoMapper.toResponse(pedido)).thenReturn(expectedResponse);

        PedidoResponse response = pedidoService.criarPedido(request);

        assertNotNull(response);
        assertEquals("Notebook", response.getProduto());
        verify(pedidoRepository).save(pedido);
        verify(kafkaProducerService).enviarEvento(pedido);
    }

    @Test
    void buscarPorId_existente_deveRetornarResponse() {
        Pedido pedido = criarPedidoBase();
        PedidoResponse expectedResponse = criarResponseBase();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(expectedResponse);

        PedidoResponse response = pedidoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void buscarPorId_inexistente_deveLancarException() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class,
                () -> pedidoService.buscarPorId(99L));
    }

    @Test
    void atualizarPedido_deveAtualizarCampos() {
        Pedido pedido = criarPedidoBase();
        AtualizarPedidoRequest request = new AtualizarPedidoRequest("Atualizado", 5, BigDecimal.valueOf(999));
        PedidoResponse expectedResponse = criarResponseBase();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);
        when(pedidoMapper.toResponse(pedido)).thenReturn(expectedResponse);

        PedidoResponse response = pedidoService.atualizarPedido(1L, request);

        assertNotNull(response);
        verify(pedidoMapper).atualizarEntity(request, pedido);
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void deletarPedido_existente_deveDeletar() {
        Pedido pedido = criarPedidoBase();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.deletarPedido(1L);

        verify(pedidoRepository).delete(pedido);
    }

    @Test
    void deletarPedido_inexistente_deveLancarException() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class,
                () -> pedidoService.deletarPedido(99L));
    }

    @Test
    void atualizarStatus_transicaoValida_deveAtualizarEPublicarEvento() {
        Pedido pedido = criarPedidoBase();
        PedidoResponse expectedResponse = criarResponseBase();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);
        when(pedidoMapper.toResponse(pedido)).thenReturn(expectedResponse);

        PedidoResponse response = pedidoService.atualizarStatus(1L, StatusPedido.PROCESSANDO);

        assertNotNull(response);
        verify(rabbitMQProducerService).enviarNotificacaoStatusAlterado(pedido, StatusPedido.PENDENTE);
        verify(eventPublisher).publicar(pedido, StatusPedido.PENDENTE, StatusPedido.PROCESSANDO);
        verify(processamentoService).processarPedidoAsync(pedido);
    }

    @Test
    void atualizarStatus_transicaoInvalida_deveLancarException() {
        Pedido pedido = criarPedidoBase();
        pedido.setStatus(StatusPedido.CONCLUIDO);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(TransicaoStatusInvalidaException.class,
                () -> pedidoService.atualizarStatus(1L, StatusPedido.PENDENTE));
    }

    private Pedido criarPedidoBase() {
        return Pedido.builder()
                .id(1L)
                .produto("Notebook")
                .quantidade(1)
                .valor(BigDecimal.valueOf(3000))
                .status(StatusPedido.PENDENTE)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    private PedidoResponse criarResponseBase() {
        return PedidoResponse.builder()
                .id(1L)
                .produto("Notebook")
                .quantidade(1)
                .valor(BigDecimal.valueOf(3000))
                .status("PENDENTE")
                .dataCriacao(LocalDateTime.now())
                .build();
    }
}
