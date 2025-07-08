package estudos.spring.avancado.estudos.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import estudos.spring.avancado.estudos.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // Usando a instância configurada

    public void enviarEvento(String topico, Pedido pedido) {
        try {
            String mensagem = objectMapper.writeValueAsString(pedido);
            kafkaTemplate.send(topico, mensagem);
            log.info("Mensagem enviada para o tópico {}: {}", topico, mensagem);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter Pedido para JSON", e);
            throw new RuntimeException("Erro ao converter Pedido para JSON: " + e.getMessage(), e);
        }
    }
}