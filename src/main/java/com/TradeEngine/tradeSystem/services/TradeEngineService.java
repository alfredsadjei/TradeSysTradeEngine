package com.TradeEngine.tradeSystem.services;

import com.TradeEngine.tradeSystem.DTOs.TradeEnginePubSub;
import com.TradeEngine.tradeSystem.utils.TradeEngineRedisClient;
import com.TradeEngine.tradeSystem.exceptions.RedisConnectionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


@Service
public class TradeEngineService{

    @Autowired
    TradeEnginePubSub tradeEnginePubSub ;

    @EventListener(ApplicationReadyEvent.class)
    public void run(){
        //have redisClient connect pub sub(subscriber) and channel
        Jedis redisSubscriber;
        try {
            //connect to redis server and send order data
            redisSubscriber = TradeEngineRedisClient.connect();
        }catch (RedisConnectionFailedException rcx){
            throw new RedisConnectionFailedException("Trade engine Subscriber connection to redis server failed.");
        }

        redisSubscriber.subscribe(tradeEnginePubSub,"orderCreated");


    }
}
