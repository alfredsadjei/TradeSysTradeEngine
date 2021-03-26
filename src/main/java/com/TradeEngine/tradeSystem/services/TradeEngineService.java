package com.TradeEngine.tradeSystem.services;

import com.TradeEngine.tradeSystem.DAOs.MarketDataRepo;
import com.TradeEngine.tradeSystem.DTOs.MarketData;
import com.TradeEngine.tradeSystem.DTOs.ProductOrder;
import com.TradeEngine.tradeSystem.utils.TradeEnginePubSub;
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
    TradeEnginePubSub tradeEnginePubSub;

    @Autowired
    MarketDataRepo marketDataRepo;

    public ProductOrder strategize(ProductOrder productOrder){

        ProductOrder exchangeOrder;

        if (productOrder.getExchange().equalsIgnoreCase("both")){
            //Retrieve market data on specific product
            MarketData orderMD1 = marketDataRepo.getExchange1DataRepository()
                    .stream()
                    .filter(md -> md.getTICKER().equalsIgnoreCase(productOrder.getProductName()))
                    .findFirst()
                    .get();


            MarketData orderMD2 = marketDataRepo.getExchange2DataRepository()
                    .stream()
                    .filter(md -> md.getTICKER().equalsIgnoreCase(productOrder.getProductName()))
                    .findFirst()
                    .get();

            //compare marketData
            String exchange = compareData(orderMD1,orderMD2, productOrder.getSide());

             exchangeOrder = new ProductOrder(productOrder.getProductName(),
                    productOrder.getPrice(),
                    productOrder.getQuantity(),
                    productOrder.getSide(), exchange);

        }else if(productOrder.getExchange().equalsIgnoreCase("ex1")){

            exchangeOrder = new ProductOrder(productOrder.getProductName(),
                    productOrder.getPrice(),
                    productOrder.getQuantity(),
                    productOrder.getSide(), "ex1");
        }else {

            exchangeOrder = new ProductOrder(productOrder.getProductName(),
                    productOrder.getPrice(),
                    productOrder.getQuantity(),
                    productOrder.getSide(), "ex2");
        }



        return exchangeOrder;

    }

    private String compareData(MarketData md1, MarketData md2, String side){
        String exchange;

        if (side.equalsIgnoreCase("BUY")){

            exchange = md1.getASK_PRICE() >= md2.getASK_PRICE() ? "ex2" : "ex1";

        }else{

            exchange = md1.getBID_PRICE() >= md2.getBID_PRICE() ? "ex1" : "ex2";
        }

        return exchange;
    }

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

        redisSubscriber.subscribe(tradeEnginePubSub,"orderCreatedT");


    }
}
