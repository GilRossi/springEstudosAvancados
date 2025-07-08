package estudos.spring.avancado.estudos.service;

import estudos.spring.avancado.estudos.dto.PedidoDTO;
import estudos.spring.avancado.estudos.kafka.KafkaProducerService;
import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService{
    private final PedidoRepository pedidoRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public Pedido criarPedido(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        pedido.setProduto(pedidoDTO.getProduto());
        pedido.setQuantidade(pedidoDTO.getQuantidade());
        pedido.setValor(pedidoDTO.getValor());
        pedido.setDataCriacao(LocalDateTime.now());

        pedido = pedidoRepository.save(pedido);

        kafkaProducerService.enviarEvento("pedidos-topic", pedido);

        return pedido;
    }
}
