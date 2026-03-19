package estudos.spring.avancado.estudos.cqrs.query;

import estudos.spring.avancado.estudos.dto.response.PedidoResponse;

public record BuscarPedidoPorIdQuery(Long id) implements Query<PedidoResponse> {
}
