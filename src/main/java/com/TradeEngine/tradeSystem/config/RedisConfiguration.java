package com.TradeEngine.tradeSystem.config;

import com.TradeEngine.tradeSystem.TradeEngineRedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Bean
    public TradeEngineRedisClient redisClientFactory(){
        return new TradeEngineRedisClient("redis-17849.c59.eu-west-1-2.ec2.cloud.redislabs.com",17849);
    }
}