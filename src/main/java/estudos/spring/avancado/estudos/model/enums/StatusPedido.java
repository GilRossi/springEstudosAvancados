package estudos.spring.avancado.estudos.model.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum StatusPedido {
    PENDENTE,
    PROCESSANDO,
    CONCLUIDO,
    CANCELADO;

    private static final Map<StatusPedido, Set<StatusPedido>> TRANSICOES_VALIDAS = Map.of(
            PENDENTE, EnumSet.of(PROCESSANDO, CANCELADO),
            PROCESSANDO, EnumSet.of(CONCLUIDO, CANCELADO),
            CONCLUIDO, EnumSet.noneOf(StatusPedido.class),
            CANCELADO, EnumSet.noneOf(StatusPedido.class)
    );

    public boolean podeTransicionarPara(StatusPedido novoStatus) {
        return TRANSICOES_VALIDAS.getOrDefault(this, EnumSet.noneOf(StatusPedido.class))
                .contains(novoStatus);
    }
}
