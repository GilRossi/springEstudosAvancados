package estudos.spring.avancado.estudos.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "pedidos-topic", groupId = "estudos-group")
    public void consumirPedido(String mensagem){
        System.out.println("Mensagem recebida do Kafka: " + mensagem);
    }
}
