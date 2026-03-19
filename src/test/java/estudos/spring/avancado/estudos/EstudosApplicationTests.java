package estudos.spring.avancado.estudos;

import estudos.spring.avancado.estudos.kafka.KafkaConsumerService;
import estudos.spring.avancado.estudos.kafka.KafkaProducerService;
import estudos.spring.avancado.estudos.rabbitmq.RabbitMQConsumerService;
import estudos.spring.avancado.estudos.rabbitmq.RabbitMQProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class EstudosApplicationTests {

	@MockitoBean
	private KafkaProducerService kafkaProducerService;

	@MockitoBean
	private KafkaConsumerService kafkaConsumerService;

	@MockitoBean
	private RabbitMQProducerService rabbitMQProducerService;

	@MockitoBean
	private RabbitMQConsumerService rabbitMQConsumerService;

	@Test
	void contextLoads() {
	}

}
