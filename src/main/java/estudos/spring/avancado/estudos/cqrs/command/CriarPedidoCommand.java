package estudos.spring.avancado.estudos.cqrs.command;

import java.math.BigDecimal;

public record CriarPedidoCommand(
        String produto,
        Integer quantidade,
        BigDecimal valor
) implements Command<Long> {
}
