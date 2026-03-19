package estudos.spring.avancado.estudos.rabbitmq;

import estudos.spring.avancado.estudos.config.RabbitMQConfig;
import estudos.spring.avancado.estudos.dto.event.PedidoStatusAlteradoMessage;
import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void enviarNotificacaoStatusAlterado(Pedido pedido, StatusPedido statusAnterior) {
        PedidoStatusAlteradoMessage message = PedidoStatusAlteradoMessage.builder()
                .pedidoId(pedido.getId())
                .produto(pedido.getProduto())
                .statusAnterior(statusAnterior.name())
                .statusNovo(pedido.getStatus().name())
                .dataEvento(LocalDateTime.now())
                .messageId(UUID.randomUUID().toString())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );

        log.info("Mensagem RabbitMQ enviada - Pedido {} status {} -> {}",
                pedido.getId(), statusAnterior, pedido.getStatus());
    }
}
