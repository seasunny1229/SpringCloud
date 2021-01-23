package com.seasunny.test.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class MQProducerTest {

    public static void main(String... args) throws Exception{

        // create producer
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("my_group");
        defaultMQProducer.setNamesrvAddr("101.132.238.214:9876");

        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(0);
        defaultMQProducer.setSendMsgTimeout(1000000);

        // start producer
        defaultMQProducer.start();
        System.out.println("start");

        // async send message
        Message msg = new Message("Jodie_topic_1023",
                "TagA",
                "OrderID188",
                "Hello world".getBytes());
        defaultMQProducer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("sent");
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });


    }






}
