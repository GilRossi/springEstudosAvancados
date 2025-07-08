package estudos.spring.avancado.estudos.service;

import estudos.spring.avancado.estudos.dto.PedidoDTO;
import estudos.spring.avancado.estudos.model.Pedido;

public interface PedidoService {
    Pedido criarPedido(PedidoDTO pedidoDTO);
}
