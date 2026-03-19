package estudos.spring.avancado.estudos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@ConditionalOnBean(ConnectionFactory.class)
public class RabbitMQConfig {

    public static final String EXCHANGE = "pedidos.exchange";
    public static final String NOTIFICACAO_QUEUE = "pedidos.notificacao.queue";
    public static final String NOTIFICACAO_DLQ = "pedidos.notificacao.dlq";
    public static final String ROUTING_KEY = "pedido.status.alterado";
    public static final String DLQ_ROUTING_KEY = "pedido.status.alterado.dlq";

    @Bean
    public DirectExchange pedidosExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue notificacaoQueue() {
        return QueueBuilder.durable(NOTIFICACAO_QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue notificacaoDlq() {
        return QueueBuilder.durable(NOTIFICACAO_DLQ).build();
    }

    @Bean
    public Binding notificacaoBinding() {
        return BindingBuilder.bind(notificacaoQueue()).to(pedidosExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(notificacaoDlq()).to(pedidosExchange()).with(DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());

        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(2000);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOff);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        template.setRetryTemplate(retryTemplate);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setDefaultRequeueRejected(false);
        factory.setMissingQueuesFatal(false);
        factory.setMismatchedQueuesFatal(false);
        factory.setAutoStartup(false);
        return factory;
    }
}
