package keysson.apis.msgsend.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * Configura um RabbitListenerContainerFactory básico para gerenciar listeners do RabbitMQ.
     * Inclui configurações adicionais para melhorar a performance e a resiliência.
     * @param connectionFactory Fábrica de conexões do RabbitMQ.
     * @return Uma instância de RabbitListenerContainerFactory configurada.
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        // Configuração de consumidores
        factory.setConcurrentConsumers(3); // Define o número inicial de consumidores
        factory.setMaxConcurrentConsumers(10); // Define o número máximo de consumidores

        // Configuração de mensagens rejeitadas
        factory.setDefaultRequeueRejected(false); // Evita reencaminhamento infinito de mensagens

        // Configuração de tempo limite
        factory.setReceiveTimeout(30000L); // Tempo limite para receber mensagens (em milissegundos)

        // Configuração de pré-busca
        factory.setPrefetchCount(20); // Define o número de mensagens pré-buscadas por consumidor

        // Configuração para não falhar quando uma fila não existe
        // Isso permite que a aplicação continue consumindo outras filas mesmo se algumas não existirem
        factory.setMissingQueuesFatal(false);

        return factory;
    }
}
