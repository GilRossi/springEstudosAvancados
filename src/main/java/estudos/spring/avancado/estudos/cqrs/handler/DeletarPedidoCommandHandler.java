package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.command.DeletarPedidoCommand;
import estudos.spring.avancado.estudos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeletarPedidoCommandHandler implements CommandHandler<DeletarPedidoCommand, Void> {

    private final PedidoService pedidoService;

    @Override
    public Void handle(DeletarPedidoCommand command) {
        log.info("[CQRS] Executando DeletarPedidoCommand para pedido: {}", command.id());
        pedidoService.deletarPedido(command.id());
        return null;
    }
}
