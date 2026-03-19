package estudos.spring.avancado.estudos.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import estudos.spring.avancado.estudos.exception.ProcessamentoException;
import estudos.spring.avancado.estudos.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.pedidos:pedidos-topic}")
    private String topicoPedidos;

    public void enviarEvento(Pedido pedido) {
        enviarEvento(topicoPedidos, pedido);
    }

    public void enviarEvento(String topico, Pedido pedido) {
        try {
            String mensagem = objectMapper.writeValueAsString(pedido);
            kafkaTemplate.send(topico, String.valueOf(pedido.getId()), mensagem);
            log.info("Mensagem enviada para o tópico {}: {}", topico, mensagem);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter Pedido para JSON", e);
            throw new ProcessamentoException("Erro ao converter Pedido para JSON: " + e.getMessage(), e);
        }
    }
}
