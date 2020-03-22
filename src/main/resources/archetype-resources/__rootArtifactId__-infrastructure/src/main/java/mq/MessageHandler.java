package ${package}.mq;

import org.springframework.stereotype.Component;

@Component
public class MessageHandler /* implements org.springframework.amqp.core.MessageListener */{

    public void handleMessage(byte[] body) {
        //TODO rabbitmq可以在消息属性定义消息体类型，然后根据类型做自动转换，转换完成后根据配置的方法名和推测的参数类型匹配方法
    }
}