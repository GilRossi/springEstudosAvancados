package estudos.spring.avancado.estudos.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.pedidos:pedidos-topic}")
    private String topicoPedidos;

    @Value("${kafka.topic.pedidos.partitions:3}")
    private int particoes;

    @Value("${kafka.topic.pedidos.replication-factor:1}")
    private int fatorReplicacao;

    @Bean
    @ConditionalOnProperty(value = "app.kafka.topic.auto-create", havingValue = "true")
    public NewTopic pedidosTopic() {
        return TopicBuilder.name(topicoPedidos)
                .partitions(particoes)
                .replicas(fatorReplicacao)
                .build();
    }
}
