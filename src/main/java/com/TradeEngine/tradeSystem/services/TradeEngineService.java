package com.TradeEngine.tradeSystem.services;

import com.TradeEngine.tradeSystem.DTOs.TradeEnginePubSub;
import com.TradeEngine.tradeSystem.TradeEngineRedisClient;
import com.TradeEngine.tradeSystem.exceptions.RedisConnectionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;


@Service
public class TradeEngineService{

    @Autowired
    TradeEngineRedisClient tradeEngineRedisClient;

    TradeEnginePubSub tradeEnginePubSub = new TradeEnginePubSub();

    @PostConstruct
    public void run(){
        //have redisClient connect pubsub(subscriber) and channel
        try {
            tradeEngineRedisClient.connect().subscribe(tradeEnginePubSub,"orderCreated");;
        }catch (RedisConnectionFailedException rcx){
            throw new RedisConnectionFailedException("Connection to redis server failed.");
        }

    }
}
