package ${package};

#if($framework.contains('rabbitmq'))
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

#end
#if($framework.contains('kafka'))
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

#end

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Message Queue publish/consume test
 *
 * @author iMinusMinus
 * @date 2020-03-14
 */
public class MqTest extends ContainerBase {

    private String message;

    @Before
    public void setUp() {
        message = String.valueOf(System.currentTimeMillis());
    }

#if($framework.contains('rabbitmq'))
    @Resource
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = {
            @QueueBinding(exchange = @Exchange(value = "amq.direct"),
                    value = @Queue(value = "queue.test"),
                    key = "iMinusMinus")})
    public void handleMessage(String text) {
        Assert.assertEquals(text, message);
    }

#end
#if($framework.contains('kafka'))
    @Resource
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = {"txTopic"})
    public void onMessage(String text) {
        Assert.assertEquals(text, message);
    }
#end

    @Test
    public void testPublish() {
#if($framework.contains('rabbitmq'))
        rabbitTemplate.convertAndSend("amq.direct", "iMinusMinus", message);
#end
    }

    @Test
    public void testTransactionalPublish() {
#if($framework.contains('kafka'))
        kafkaTemplate.executeInTransaction(kafkaTemplate -> {
            kafkaTemplate.send("txTopic", message);
            return true;
        });
#end
    }

}
