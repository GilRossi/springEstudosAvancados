package estudos.spring.avancado.estudos.cqrs.command;

public record DeletarPedidoCommand(Long id) implements Command<Void> {
}
