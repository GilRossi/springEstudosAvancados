package estudos.spring.avancado.estudos.mapper;

import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PaginacaoResponse;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public Pedido toEntity(CriarPedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setProduto(request.getProduto());
        pedido.setQuantidade(request.getQuantidade());
        pedido.setValor(request.getValor());
        return pedido;
    }

    public PedidoResponse toResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .produto(pedido.getProduto())
                .quantidade(pedido.getQuantidade())
                .valor(pedido.getValor())
                .status(pedido.getStatus().name())
                .dataCriacao(pedido.getDataCriacao())
                .dataAtualizacao(pedido.getDataAtualizacao())
                .build();
    }

    public List<PedidoResponse> toResponseList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(this::toResponse)
                .toList();
    }

    public PaginacaoResponse<PedidoResponse> toPaginacaoResponse(Page<Pedido> page) {
        return PaginacaoResponse.<PedidoResponse>builder()
                .conteudo(toResponseList(page.getContent()))
                .pagina(page.getNumber())
                .tamanhoPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .ultima(page.isLast())
                .build();
    }

    public void atualizarEntity(AtualizarPedidoRequest request, Pedido pedido) {
        if (request.getProduto() != null && !request.getProduto().isBlank()) {
            pedido.setProduto(request.getProduto());
        }
        if (request.getQuantidade() != null) {
            pedido.setQuantidade(request.getQuantidade());
        }
        if (request.getValor() != null) {
            pedido.setValor(request.getValor());
        }
    }
}
