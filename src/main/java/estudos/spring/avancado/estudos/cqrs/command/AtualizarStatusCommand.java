package estudos.spring.avancado.estudos.cqrs.command;

import estudos.spring.avancado.estudos.model.enums.StatusPedido;

public record AtualizarStatusCommand(
        Long id,
        StatusPedido novoStatus
) implements Command<Void> {
}
