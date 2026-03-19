package estudos.spring.avancado.estudos.event;

import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PedidoStatusAlteradoEvent extends ApplicationEvent {

    private final Pedido pedido;
    private final StatusPedido statusAnterior;
    private final StatusPedido statusNovo;

    public PedidoStatusAlteradoEvent(Object source, Pedido pedido,
                                     StatusPedido statusAnterior, StatusPedido statusNovo) {
        super(source);
        this.pedido = pedido;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
    }
}
