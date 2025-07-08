package estudos.spring.avancado.estudos.service;

import estudos.spring.avancado.estudos.model.Pedido;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProcessamentoService {
    @Async("taskExecutor")
    public void processarPedidoAsync(Pedido pedido){
        System.out.println("Processando pedido: "
                + pedido.getId()
                + " em thread: "
                + Thread.currentThread().getName());
    }
}
