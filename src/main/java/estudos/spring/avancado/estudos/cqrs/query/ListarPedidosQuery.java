package estudos.spring.avancado.estudos.cqrs.query;

import estudos.spring.avancado.estudos.dto.response.PaginacaoResponse;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;

public record ListarPedidosQuery(
        int pagina,
        int tamanhoPagina
) implements Query<PaginacaoResponse<PedidoResponse>> {
}
