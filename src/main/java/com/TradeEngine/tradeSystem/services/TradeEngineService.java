package com.TradeEngine.tradeSystem.services;

import com.TradeEngine.tradeSystem.DTOs.TradeEnginePubSub;
import com.TradeEngine.tradeSystem.TradeEngineRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class TradeEngineService{

    @Autowired
    TradeEngineRedisClient tradeEngineRedisClient;

    TradeEnginePubSub tradeEnginePubSub = new TradeEnginePubSub();

    @PostConstruct
    public void run(){
        //have redisClient connect pubsub(subscriber) and channel
        tradeEngineRedisClient.connect().subscribe(tradeEnginePubSub,"orderCreated");
    }
}
