package estudos.spring.avancado.estudos.service;

import estudos.spring.avancado.estudos.dto.request.AtualizarPedidoRequest;
import estudos.spring.avancado.estudos.dto.request.CriarPedidoRequest;
import estudos.spring.avancado.estudos.dto.response.PaginacaoResponse;
import estudos.spring.avancado.estudos.dto.response.PedidoResponse;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;

public interface PedidoService {
    PedidoResponse criarPedido(CriarPedidoRequest request);
    PedidoResponse buscarPorId(Long id);
    PaginacaoResponse<PedidoResponse> listarTodos(int pagina, int tamanhoPagina);
    PedidoResponse atualizarPedido(Long id, AtualizarPedidoRequest request);
    void deletarPedido(Long id);
    PedidoResponse atualizarStatus(Long id, StatusPedido novoStatus);
}
