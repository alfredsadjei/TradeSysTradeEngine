package com.TradeEngine.tradeSystem;

import com.TradeEngine.tradeSystem.utils.RedisServer;
import redis.clients.jedis.Jedis;

public class TradeEngineRedisClient {

    private final String hostName;
    private final int port;
    private Jedis jedis;

    public TradeEngineRedisClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public Jedis connect(){

        if (this.jedis == null){
            this.jedis = new Jedis(hostName,port);
            jedis.auth(RedisServer.SERVER_KEY.getKeyVal());

            System.out.println("Connected to redis server...");
            return this.jedis;

        }

        return this.jedis;
    }
}
