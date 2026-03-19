package estudos.spring.avancado.estudos.observer;

import estudos.spring.avancado.estudos.event.PedidoStatusAlteradoEvent;

public interface PedidoStatusObserver {
    void aoAlterarStatus(PedidoStatusAlteradoEvent event);
}
