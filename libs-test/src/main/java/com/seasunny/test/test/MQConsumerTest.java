package com.seasunny.test.test;



import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class MQConsumerTest {

    public static void main(String... args) throws Exception{

        // create consumer
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("my_group");
        defaultMQPushConsumer.setNamesrvAddr("101.132.238.214:9876");

        defaultMQPushConsumer.setConsumeTimeout(1000000);

        // subscribe topic
        defaultMQPushConsumer.subscribe("Jodie_topic_1023", "*");

        // set listener
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), list);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // start
        defaultMQPushConsumer.start();
        System.out.println("start");
    }


}
