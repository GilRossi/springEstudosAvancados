package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.query.BuscarPedidoPorIdQuery;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuscarPedidoPorIdQueryHandler implements QueryHandler<BuscarPedidoPorIdQuery, PedidoResponse> {

    private final PedidoService pedidoService;

    @Override
    public PedidoResponse handle(BuscarPedidoPorIdQuery query) {
        log.debug("[CQRS] Executando BuscarPedidoPorIdQuery para id: {}", query.id());
        return pedidoService.buscarPorId(query.id());
    }
}
