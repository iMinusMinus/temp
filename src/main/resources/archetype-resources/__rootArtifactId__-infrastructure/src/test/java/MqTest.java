package ${package};

#if($framework.contains('rabbitmq'))
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

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
            @QueueBinding(exchange = @Exchange(name = "amq.direct"),
                    value = @Queue(name = "amqp.queue.test.unique"),
                    key = {"iMinusMinus"})})
    public void handleMessage(String text) {
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
        //TODO
    }

}
