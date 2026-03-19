package estudos.spring.avancado.estudos.event;

import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoStatusEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publicar(Pedido pedido, StatusPedido statusAnterior, StatusPedido statusNovo) {
        log.debug("Publicando evento de alteração de status: pedido={}, {} -> {}",
                pedido.getId(), statusAnterior, statusNovo);
        PedidoStatusAlteradoEvent event = new PedidoStatusAlteradoEvent(this, pedido, statusAnterior, statusNovo);
        eventPublisher.publishEvent(event);
    }
}
