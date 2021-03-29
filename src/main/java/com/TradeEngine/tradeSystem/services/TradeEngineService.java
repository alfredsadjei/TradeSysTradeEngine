package com.TradeEngine.tradeSystem.services;

import com.TradeEngine.tradeSystem.DTOs.OpenOrder;
import com.TradeEngine.tradeSystem.DTOs.ProductOrder;
import com.TradeEngine.tradeSystem.utils.OrderBookService;
import com.TradeEngine.tradeSystem.utils.TradeEnginePubSub;
import com.TradeEngine.tradeSystem.utils.TradeEngineRedisClient;
import com.TradeEngine.tradeSystem.exceptions.RedisConnectionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@Service
public class TradeEngineService{

    List<ProductOrder> OrderList = new ArrayList<>();

    @Autowired
    TradeEnginePubSub tradeEnginePubSub;

    public List<ProductOrder> strategize(ProductOrder productOrder) throws IOException {

        if (productOrder.getExchange().equalsIgnoreCase("both")){
            //Retrieve orderBook data on specific product
            List<OpenOrder> orderD1 = getOrderBookData( productOrder,"ex1");


            List<OpenOrder> orderD2 = getOrderBookData( productOrder,"ex2");

            //compare marketData and return list
            OrderList = compareData(orderD1,orderD2, productOrder);

        }else if(productOrder.getExchange().equalsIgnoreCase("ex1")){

           OrderList.add( new ProductOrder(productOrder.getProductName(),
                    productOrder.getPrice(),
                    productOrder.getQuantity(),
                    productOrder.getSide(), "ex1"));
        }else {

             OrderList.add( new ProductOrder(productOrder.getProductName(),
                    productOrder.getPrice(),
                    productOrder.getQuantity(),
                    productOrder.getSide(), "ex2"));
        }

        return OrderList;

    }

    private List<ProductOrder> compareData(List<OpenOrder> d1, List<OpenOrder> d2, ProductOrder order){
        List<ProductOrder> exchangeOrderList = new ArrayList<>();

        if (order.getSide().equalsIgnoreCase("BUY")){

            //FIRST GET AVG ASK PRICE FROM BOTH EXCHANGES
            double avg_ask1 = get_avg_ask(d1);
            double avg_ask2 = get_avg_ask(d2);

            if (avg_ask1 <= avg_ask2){
                //get available quantity from ex1
                int available = getAvailableQuantity(d1, order.getSide());

                if (available >= order.getQuantity()){
                    //no need to split
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex1"));
                }else{
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , available
                            , order.getSide()
                            ,"ex1"));

                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex2"));
                }

            }else{
                //get available quantity from ex2
                int available = getAvailableQuantity(d2, order.getSide());

                if (available >= order.getQuantity()){

                    //no need to split
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex2"));
                }else{
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , available
                            , order.getSide()
                            ,"ex2"));

                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex1"));
                }
            }


        }else{
            //FIRST GET AVG BID PRICE FROM BOTH EXCHANGES
            double avg_bid1 = get_avg_bid(d1);
            double avg_bid2 = get_avg_bid(d2);

            if (avg_bid1 >= avg_bid2){
                //get available quantity from ex1
                int available = getAvailableQuantity(d1, order.getSide());

                if (available >= order.getQuantity()){
                    //no need to split
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex1"));
                }else{
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , available
                            , order.getSide()
                            ,"ex1"));

                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex2"));
                }

            }else{
                //get available quantity from ex2
                int available = getAvailableQuantity(d2, order.getSide());

                if (available >= order.getQuantity()){

                    //no need to split
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex2"));
                }else{
                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , available
                            , order.getSide()
                            ,"ex2"));

                    exchangeOrderList.add(new ProductOrder(order.getProductName()
                            , order.getPrice()
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex1"));
                }
            }

        }


        return exchangeOrderList;
    }


    private List<OpenOrder> getOrderBookData(ProductOrder order, String exchange) throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://exchange.matraining.com/orderbook/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        OrderBookService orderBookService = retrofit.create(OrderBookService.class);

        Call<List<OpenOrder>> orderBook = exchange.equals("ex1") ?
                orderBookService.getData("https://exchange.matraining.com/orderbook/"
                +order.getProductName()
                +"/") :
                orderBookService.getData("https://exchange2.matraining.com/orderbook/"
                +order.getProductName()
                +"/");

        return orderBook.execute().body();
    }

    private double get_avg_ask(List<OpenOrder> data){

        double sum_ask = data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase("sell")
                        && openOrder.getCumulativeQuantity() < openOrder.getQuantity())
                .map(OpenOrder::getPrice)
                .reduce(Double::sum).get();

        double count_ask = data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase("sell")
                        && openOrder.getExecutions().isEmpty())
                .count();

        return sum_ask/count_ask;

    }

    private double get_avg_bid(List<OpenOrder> data){
        double sum_bid = data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase("buy")
                        && openOrder.getCumulativeQuantity() < openOrder.getQuantity())
                .map(OpenOrder::getPrice)
                .reduce(Double::sum).get();

        double count_bid = data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase("buy")
                        && openOrder.getExecutions().isEmpty())
                .count();

        return sum_bid/count_bid;

    }

    private int getAvailableQuantity(List<OpenOrder> data, String side){

        return data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase(side.equalsIgnoreCase("buy")?
                        "sell" : "buy")
                        && openOrder.getCumulativeQuantity() < openOrder.getQuantity())
                .map(openOrder -> openOrder.getQuantity() - openOrder.getCumulativeQuantity())
                .reduce(Integer::sum).get();
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
