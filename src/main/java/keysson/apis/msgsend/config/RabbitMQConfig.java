package keysson.apis.msgsend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue empresaQueue() {
        return new Queue("empresa.fila", true);
    }

    @Bean
    public Queue funcionarioQueue() {
        return new Queue("funcionario.fila", true);
    }

    @Bean
    public Queue funcionarioClienteQueue() {
        return new Queue("funcionario-cliente.fila", true);
    }

    @Bean
    public Queue alteraStatusQueue() {
        return new Queue("alteraStatus.fila", true);
    }

    @Bean
    public Queue passwordResetQueue() {
        return new Queue("password.reset.queue", true);
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);

        factory.setDefaultRequeueRejected(false);

        factory.setReceiveTimeout(30000L);

        factory.setPrefetchCount(20);

        factory.setMissingQueuesFatal(false);

        return factory;
    }
}
