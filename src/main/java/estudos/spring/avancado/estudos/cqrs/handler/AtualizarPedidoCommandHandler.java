package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.command.AtualizarPedidoCommand;
import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AtualizarPedidoCommandHandler implements CommandHandler<AtualizarPedidoCommand, Void> {

    private final PedidoService pedidoService;

    @Override
    public Void handle(AtualizarPedidoCommand command) {
        log.info("[CQRS] Executando AtualizarPedidoCommand para pedido: {}", command.id());

        AtualizarPedidoRequest request = new AtualizarPedidoRequest(
                command.produto(),
                command.quantidade(),
                command.valor()
        );

        pedidoService.atualizarPedido(command.id(), request);
        return null;
    }
}
