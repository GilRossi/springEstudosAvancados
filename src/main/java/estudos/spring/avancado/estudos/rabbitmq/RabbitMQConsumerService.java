package estudos.spring.avancado.estudos.rabbitmq;

import estudos.spring.avancado.estudos.config.RabbitMQConfig;
import estudos.spring.avancado.estudos.dto.event.PedidoStatusAlteradoMessage;
import estudos.spring.avancado.estudos.model.MensagemProcessada;
import estudos.spring.avancado.estudos.repository.MensagemProcessadaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumerService {

    private final MensagemProcessadaRepository mensagemRepository;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICACAO_QUEUE, autoStartup = "false")
    @Transactional
    public void processarNotificacao(PedidoStatusAlteradoMessage message) {
        if (mensagemRepository.existsByMessageId(message.getMessageId())) {
            log.warn("Mensagem ja processada: {}", message.getMessageId());
            return;
        }

        log.info("Processando notificacao RabbitMQ - Pedido {} status {} -> {}",
                message.getPedidoId(), message.getStatusAnterior(), message.getStatusNovo());

        simularEnvioNotificacao(message);

        mensagemRepository.save(MensagemProcessada.builder()
                .messageId(message.getMessageId())
                .tipo("NOTIFICACAO_STATUS")
                .build());

        log.info("Notificacao processada com sucesso - messageId: {}", message.getMessageId());
    }

    private void simularEnvioNotificacao(PedidoStatusAlteradoMessage message) {
        log.info("[NOTIFICACAO] Pedido #{} ({}) teve status alterado de {} para {}",
                message.getPedidoId(),
                message.getProduto(),
                message.getStatusAnterior(),
                message.getStatusNovo());
    }
}
