package com.TradeEngine.tradeSystem.services;

import com.TradeEngine.tradeSystem.DTOs.TradeEnginePubSub;
import com.TradeEngine.tradeSystem.TradeEngineRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;



@Component
public class TradeEngineService implements ApplicationRunner {

    @Autowired
    TradeEngineRedisClient tradeEngineRedisClient;

    TradeEnginePubSub tradeEnginePubSub = new TradeEnginePubSub();

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //have redisClient connect pubsub(subscriber) and channel
        tradeEngineRedisClient.connect().subscribe(tradeEnginePubSub,"orderCreated");
    }
}
