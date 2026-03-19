package estudos.spring.avancado.estudos.service.strategy;

import estudos.spring.avancado.estudos.model.Pedido;

public interface ProcessamentoStrategy {
    void processar(Pedido pedido);
    boolean suporta(Pedido pedido);
}
