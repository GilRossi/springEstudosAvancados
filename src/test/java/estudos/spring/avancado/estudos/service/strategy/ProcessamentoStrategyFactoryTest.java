package estudos.spring.avancado.estudos.service.strategy;

import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessamentoStrategyFactoryTest {

    private ProcessamentoStrategyFactory factory;

    @BeforeEach
    void setUp() {
        List<ProcessamentoStrategy> strategies = List.of(
                new ProcessamentoAltoValorStrategy(),
                new ProcessamentoBaixoValorStrategy()
        );
        factory = new ProcessamentoStrategyFactory(strategies);
    }

    @Test
    void obterEstrategia_pedidoAltoValor_deveRetornarAltoValorStrategy() {
        Pedido pedido = Pedido.builder()
                .id(1L).produto("Notebook").quantidade(1)
                .valor(BigDecimal.valueOf(5000))
                .status(StatusPedido.PROCESSANDO)
                .dataCriacao(LocalDateTime.now())
                .build();

        ProcessamentoStrategy strategy = factory.obterEstrategia(pedido);

        assertInstanceOf(ProcessamentoAltoValorStrategy.class, strategy);
    }

    @Test
    void obterEstrategia_pedidoBaixoValor_deveRetornarBaixoValorStrategy() {
        Pedido pedido = Pedido.builder()
                .id(2L).produto("Mouse").quantidade(3)
                .valor(BigDecimal.valueOf(50))
                .status(StatusPedido.PROCESSANDO)
                .dataCriacao(LocalDateTime.now())
                .build();

        ProcessamentoStrategy strategy = factory.obterEstrategia(pedido);

        assertInstanceOf(ProcessamentoBaixoValorStrategy.class, strategy);
    }

    @Test
    void obterEstrategia_pedidoNoLimite_deveRetornarBaixoValorStrategy() {
        Pedido pedido = Pedido.builder()
                .id(3L).produto("Teclado").quantidade(1)
                .valor(BigDecimal.valueOf(1000))
                .status(StatusPedido.PROCESSANDO)
                .dataCriacao(LocalDateTime.now())
                .build();

        ProcessamentoStrategy strategy = factory.obterEstrategia(pedido);

        assertInstanceOf(ProcessamentoBaixoValorStrategy.class, strategy);
    }
}
