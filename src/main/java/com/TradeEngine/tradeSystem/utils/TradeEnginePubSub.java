package com.TradeEngine.tradeSystem.utils;

import com.TradeEngine.tradeSystem.DTOs.ProductOrder;
import com.TradeEngine.tradeSystem.services.TradeEngineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


@Service
public class TradeEnginePubSub extends JedisPubSub {

    @Autowired
    TradeEngineService tradeEngineService;

    @Override
    public void onMessage(String channel, String message) {
       //receive message deserialize and apply trade engine business logic
        ObjectMapper objectMapper = new ObjectMapper();
        ProductOrder orderMessage;

        try {

            orderMessage = objectMapper.readValue(message,ProductOrder.class);

            ProductOrder exOrder = tradeEngineService.strategize(orderMessage);

            System.out.println(exOrder);

            //send order to exchange connectivity
            //Can't use the same jedis instance for pushing to queue
            Jedis newJ = new Jedis("redis-17849.c59.eu-west-1-2.ec2.cloud.redislabs.com",17849);
            newJ.auth(RedisServer.SERVER_KEY.getKeyVal());

            newJ.lpush("orderCreatedQ",objectMapper.writeValueAsString(exOrder));

            System.out.println("Pushed");

            newJ.close();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }


}
