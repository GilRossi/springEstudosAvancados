package estudos.spring.avancado.estudos.service;

import estudos.spring.avancado.estudos.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public abstract class ProcessamentoService {

    @Async("taskExecutor")
    public void processarPedidoAsync(Pedido pedido) {
        log.info("Iniciando processamento assíncrono do pedido {} na thread: {}",
                pedido.getId(), Thread.currentThread().getName());
        validar(pedido);
        executarProcessamento(pedido);
        finalizarProcessamento(pedido);
    }

    protected void validar(Pedido pedido) {
        log.debug("Validando pedido {} para processamento", pedido.getId());
    }

    protected abstract void executarProcessamento(Pedido pedido);

    protected void finalizarProcessamento(Pedido pedido) {
        log.info("Processamento do pedido {} finalizado na thread: {}",
                pedido.getId(), Thread.currentThread().getName());
    }
}
