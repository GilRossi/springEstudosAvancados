package estudos.spring.avancado.estudos.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPedidoTest {

    @Test
    void pendente_podeTransicionarParaProcessando() {
        assertTrue(StatusPedido.PENDENTE.podeTransicionarPara(StatusPedido.PROCESSANDO));
    }

    @Test
    void pendente_podeTransicionarParaCancelado() {
        assertTrue(StatusPedido.PENDENTE.podeTransicionarPara(StatusPedido.CANCELADO));
    }

    @Test
    void pendente_naoPodeTransicionarParaConcluido() {
        assertFalse(StatusPedido.PENDENTE.podeTransicionarPara(StatusPedido.CONCLUIDO));
    }

    @Test
    void processando_podeTransicionarParaConcluido() {
        assertTrue(StatusPedido.PROCESSANDO.podeTransicionarPara(StatusPedido.CONCLUIDO));
    }

    @Test
    void processando_podeTransicionarParaCancelado() {
        assertTrue(StatusPedido.PROCESSANDO.podeTransicionarPara(StatusPedido.CANCELADO));
    }

    @Test
    void concluido_naoPodeTransicionar() {
        assertFalse(StatusPedido.CONCLUIDO.podeTransicionarPara(StatusPedido.PENDENTE));
        assertFalse(StatusPedido.CONCLUIDO.podeTransicionarPara(StatusPedido.PROCESSANDO));
        assertFalse(StatusPedido.CONCLUIDO.podeTransicionarPara(StatusPedido.CANCELADO));
    }

    @Test
    void cancelado_naoPodeTransicionar() {
        assertFalse(StatusPedido.CANCELADO.podeTransicionarPara(StatusPedido.PENDENTE));
        assertFalse(StatusPedido.CANCELADO.podeTransicionarPara(StatusPedido.PROCESSANDO));
        assertFalse(StatusPedido.CANCELADO.podeTransicionarPara(StatusPedido.CONCLUIDO));
    }
}
