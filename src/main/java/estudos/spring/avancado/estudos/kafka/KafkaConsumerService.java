package estudos.spring.avancado.estudos.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import estudos.spring.avancado.estudos.model.Pedido;
import estudos.spring.avancado.estudos.model.enums.StatusPedido;
import estudos.spring.avancado.estudos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final PedidoService pedidoService;

    @KafkaListener(topics = "${kafka.topic.pedidos:pedidos-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumirPedido(String mensagem) {
        log.info("Mensagem recebida do Kafka: {}", mensagem);
        try {
            Pedido pedido = objectMapper.readValue(mensagem, Pedido.class);
            log.info("Pedido {} desserializado com sucesso - Produto: {}, Valor: R${}",
                    pedido.getId(), pedido.getProduto(), pedido.getValor());

            pedidoService.atualizarStatus(pedido.getId(), StatusPedido.PROCESSANDO);
        } catch (JsonProcessingException e) {
            log.error("Erro ao desserializar mensagem do Kafka: {}", mensagem, e);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem do Kafka: {}", e.getMessage(), e);
        }
    }
}
