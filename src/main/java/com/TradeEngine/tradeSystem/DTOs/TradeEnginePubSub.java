package com.TradeEngine.tradeSystem.DTOs;

import com.TradeEngine.tradeSystem.DAOs.MarketDataRepo;
import com.TradeEngine.tradeSystem.TradeEngineRedisClient;
import com.TradeEngine.tradeSystem.utils.RedisServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


@Component
public class TradeEnginePubSub extends JedisPubSub {

    @Autowired
    MarketDataRepo marketDataRepo;

    @Override
    public void onMessage(String channel, String message) {
       //receive message deserialize and apply trade engine business logic
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.readValue(message,ProductOrder.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }




        //send order to exchange connectivity
        //Can't use the same jedis instance for pushing to queue
        Jedis newJ = new Jedis("redis-17849.c59.eu-west-1-2.ec2.cloud.redislabs.com",17849);
        newJ.auth(RedisServer.SERVER_KEY.getKeyVal());

        newJ.lpush("orderCreatedQ",message);

        newJ.close();

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
    }
}
