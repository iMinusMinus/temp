package ${package};

#if($frameworks.contains('rabbitmq'))
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;

import com.rabbitmq.client.ConnectionFactory;
#end
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Message Queue config
 *
 * @author iMinusMinus
 * @date 2020-03-14
 */
@Configuration
#if($frameworks.contains('rabbitmq'))
@EnableRabbit
#end
public class MqConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("spring.rabbitmq.username")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtualHost}")
    private String virtualHost;

#if($frameworks.contains('rabbitmq'))
    @Bean
    public ConnectionFactory rabbitConnectionFactory() throws Exception {
        RabbitConnectionFactoryBean connectionFactory = new RabbitConnectionFactoryBean();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUserName(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.afterPropertiesSet();
        CachingConnectionFactory rabbitConnectionFactory = new CachingConnectionFactory(connectionFactory.getObject());
        return rabbitConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(rabbitConnectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory) {
        SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        rabbitListenerContainerFactory.setConnectionFactory(rabbitConnectionFactory);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

#end
}