package estudos.spring.avancado.estudos.observer;

import estudos.spring.avancado.estudos.event.PedidoStatusAlteradoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificacaoPedidoObserver implements PedidoStatusObserver {

    @Override
    @EventListener
    public void aoAlterarStatus(PedidoStatusAlteradoEvent event) {
        log.info("[OBSERVER-NOTIFICACAO] Notificação enviada para pedido {}: status alterado para {}",
                event.getPedido().getId(),
                event.getStatusNovo());
    }
}
