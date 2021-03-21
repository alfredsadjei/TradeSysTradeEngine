package com.TradeEngine.tradeSystem.DTOs;

import com.TradeEngine.tradeSystem.DAOs.MarketDataRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPubSub;

public class TradeEnginePubSub extends JedisPubSub {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MarketDataRepo marketDataRepo;

    @Override
    public void onMessage(String channel, String message) {
       //receive message deserialize and apply trade engine business logic
        try {
            System.out.println(objectMapper.readValue(message,ProductOrder.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        //send order to exchange connectivity
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
