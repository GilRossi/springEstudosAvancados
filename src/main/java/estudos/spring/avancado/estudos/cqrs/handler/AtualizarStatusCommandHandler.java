package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.command.AtualizarStatusCommand;
import estudos.spring.avancado.estudos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AtualizarStatusCommandHandler implements CommandHandler<AtualizarStatusCommand, Void> {

    private final PedidoService pedidoService;

    @Override
    public Void handle(AtualizarStatusCommand command) {
        log.info("[CQRS] Executando AtualizarStatusCommand - Pedido {} para {}", command.id(), command.novoStatus());
        pedidoService.atualizarStatus(command.id(), command.novoStatus());
        return null;
    }
}
