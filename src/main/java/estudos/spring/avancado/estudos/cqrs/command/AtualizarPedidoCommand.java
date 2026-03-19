package estudos.spring.avancado.estudos.cqrs.command;

import java.math.BigDecimal;

public record AtualizarPedidoCommand(
        Long id,
        String produto,
        Integer quantidade,
        BigDecimal valor
) implements Command<Void> {
}
