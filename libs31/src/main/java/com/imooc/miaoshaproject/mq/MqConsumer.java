package com.imooc.miaoshaproject.mq;

import com.alibaba.fastjson.JSON;
import com.imooc.miaoshaproject.dao.ItemDOMapper;
import com.imooc.miaoshaproject.dao.ItemStockDOMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by hzllb on 2019/2/23.
 *
 * create default MQ push consumer
 * receive messages from producer
 *
 * decrease stocks to consume messages
 *
 */
@Component
public class MqConsumer {


    private DefaultMQPushConsumer consumer;

    @Value("${mq.nameserver.addr}")
    private String nameAddr;

    @Value("${mq.topicname}")
    private String topicName;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @PostConstruct
    public void init() throws MQClientException {

        // create consumer
        consumer = new DefaultMQPushConsumer("stock_consumer_group");
        consumer.setNamesrvAddr(nameAddr);
        consumer.subscribe(topicName,"*");

        // register message listener
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                //实现库存真正到数据库内扣减的逻辑

                // get message
                Message msg = msgs.get(0);
                String jsonString  = new String(msg.getBody());

                // parse message json to hash map
                Map<String,Object>map = JSON.parseObject(jsonString, Map.class);

                // get item id and amount
                Integer itemId = (Integer) map.get("itemId");
                Integer amount = (Integer) map.get("amount");

                // decrease stock
                itemStockDOMapper.decreaseStock(itemId,amount);

                // has consumed
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // start consumer
        consumer.start();

    }
}
