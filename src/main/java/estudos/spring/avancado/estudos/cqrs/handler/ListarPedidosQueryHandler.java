package estudos.spring.avancado.estudos.cqrs.handler;

import estudos.spring.avancado.estudos.cqrs.query.ListarPedidosQuery;
import estudos.spring.avancado.estudos.dto.response.PaginacaoResponse;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListarPedidosQueryHandler implements QueryHandler<ListarPedidosQuery, PaginacaoResponse<PedidoResponse>> {

    private final PedidoService pedidoService;

    @Override
    public PaginacaoResponse<PedidoResponse> handle(ListarPedidosQuery query) {
        log.debug("[CQRS] Executando ListarPedidosQuery - página: {}, tamanho: {}", query.pagina(), query.tamanhoPagina());
        return pedidoService.listarTodos(query.pagina(), query.tamanhoPagina());
    }
}
