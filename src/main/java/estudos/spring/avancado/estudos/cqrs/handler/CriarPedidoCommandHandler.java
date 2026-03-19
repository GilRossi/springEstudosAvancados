package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.command.CriarPedidoCommand;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CriarPedidoCommandHandler implements CommandHandler<CriarPedidoCommand, Long> {

    private final PedidoService pedidoService;

    @Override
    public Long handle(CriarPedidoCommand command) {
        log.info("[CQRS] Executando CriarPedidoCommand para produto: {}", command.produto());

        CriarPedidoRequest request = new CriarPedidoRequest(
                command.produto(),
                command.quantidade(),
                command.valor()
        );

        PedidoResponse response = pedidoService.criarPedido(request);
        return response.getId();
    }
}
