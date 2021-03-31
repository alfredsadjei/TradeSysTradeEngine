package com.TradeEngine.tradeSystem.services;

import com.TradeEngine.tradeSystem.DTOs.OpenOrder;
import com.TradeEngine.tradeSystem.DTOs.ProductOrder;
import com.TradeEngine.tradeSystem.exceptions.DataNotFoundException;
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
import java.util.Arrays;
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

            //FIRST GET MIN ASK PRICE FROM BOTH EXCHANGES
            double min_ask1 = get_min_ask(d1);
            double min_ask2 = get_min_ask(d2);

            if (min_ask1 <= min_ask2){

                //get available quantity from ex1
                int available = getAvailableQuantity(d1, order.getSide());

                if (available >= order.getQuantity()){
                    List<ProductOrder> tempList = new ArrayList<>();

                    //no need to split
                    tempList.add(new ProductOrder(order.getProductName()
                            //if original price is greater that ask price, set order price to ask price
                            //else if ask price is greater order price by .16 or more, keep original order price
                            //else set order price original order price + 0.25
                            , order.getPrice() >= min_ask1? min_ask1
                            : min_ask1 - order.getPrice()>0.25?order.getPrice():order.getPrice()+(min_ask1 - order.getPrice())
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex1"));

                    exchangeOrderList = tempList;
                }else{

                    List<ProductOrder> tempList = new ArrayList<>();

                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() >= min_ask1? min_ask1
                            : min_ask1 - order.getPrice() > 0.25? order.getPrice():order.getPrice() + (min_ask1 - order.getPrice())
                            , available
                            , order.getSide()
                            ,"ex1"));

                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() >= min_ask2? min_ask2
                            : min_ask2 - order.getPrice() > 0.25? order.getPrice():order.getPrice() + (min_ask2 - order.getPrice())
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex2"));

                    exchangeOrderList = tempList;
                }

            }else{

                //get available quantity from ex2
                int available = getAvailableQuantity(d2, order.getSide());

                if (available >= order.getQuantity()){
                    List<ProductOrder> tempList = new ArrayList<>();
                    //no need to split
                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() >= min_ask2? min_ask2
                            : min_ask2 - order.getPrice() > 0.25? order.getPrice():order.getPrice() + (min_ask2 - order.getPrice())
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex2"));

                    exchangeOrderList = tempList;
                }else{
                    List<ProductOrder> tempList = new ArrayList<>();

                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() >= min_ask2? min_ask2
                            : min_ask2 - order.getPrice() > 0.25? order.getPrice():order.getPrice() + (min_ask2 - order.getPrice())
                            , available
                            , order.getSide()
                            ,"ex2"));

                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() >= min_ask1? min_ask1
                            : min_ask1 - order.getPrice() > 0.25? order.getPrice():order.getPrice() + (min_ask1 - order.getPrice())
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex1"));

                    exchangeOrderList = tempList;

                }
            }

        }else{



            //FIRST GET MAX BID PRICE FROM BOTH EXCHANGES
            double max_bid1 = get_max_bid(d1);
            double max_bid2 = get_max_bid(d2);


            if (max_bid1 >= max_bid2){
                //get available quantity from ex1
                int available = getAvailableQuantity(d1, order.getSide());

                if (available >= order.getQuantity()){

                    List<ProductOrder> tempList = new ArrayList<>();
                    //no need to split
                    tempList.add(new ProductOrder(order.getProductName()
                            // if original ask price is less than max bid price, set order as max bid price
                            //else if the difference between original order price and max bid price is greater than 0.25
                            //set order price as max bid price
                            //else set order price to the order price - the difference
                            , order.getPrice() <= max_bid1? max_bid1
                            : order.getPrice() - max_bid1 > 0.25 ? max_bid1
                            : order.getPrice() - (order.getPrice() - max_bid1)
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex1"));

                    exchangeOrderList = tempList;
                }else{

                    List<ProductOrder> tempList = new ArrayList<>();
                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() <= max_bid1? max_bid1
                            : order.getPrice() - max_bid1 > 0.25 ? max_bid1
                            : order.getPrice() - (order.getPrice() - max_bid1)
                            , available
                            , order.getSide()
                            ,"ex1"));

                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() <= max_bid2? max_bid2
                            : order.getPrice() - max_bid2 > 0.25 ? max_bid2
                            : order.getPrice() - (order.getPrice() - max_bid2)
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex2"));

                    exchangeOrderList = tempList;
                }

            }else{
                //get available quantity from ex2
                int available = getAvailableQuantity(d2, order.getSide());

                if (available >= order.getQuantity()){
                    List<ProductOrder> tempList = new ArrayList<>();

                    //no need to split
                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() <= max_bid2? max_bid2
                            : order.getPrice() - max_bid2 > 0.25 ? max_bid2
                            : order.getPrice() - (order.getPrice() - max_bid2)
                            , order.getQuantity()
                            , order.getSide()
                            ,"ex2"));

                    exchangeOrderList = tempList;
                }else{
                    List<ProductOrder> tempList = new ArrayList<>();
                    tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() <= max_bid2? max_bid2
                            : order.getPrice() - max_bid2 > 0.25 ? max_bid2
                            : order.getPrice() - (order.getPrice() - max_bid2)
                            , available
                            , order.getSide()
                            ,"ex2"));

                   tempList.add(new ProductOrder(order.getProductName()
                            , order.getPrice() <= max_bid1? max_bid1
                            : order.getPrice() - max_bid1 > 0.25 ? max_bid1
                            : order.getPrice() - (order.getPrice() - max_bid1)
                            , order.getQuantity() - available
                            , order.getSide()
                            ,"ex1"));

                   exchangeOrderList = tempList;
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
                ) :
                orderBookService.getData("https://exchange2.matraining.com/orderbook/"
                +order.getProductName()
                );

        return orderBook.execute().body();
    }

    private double get_min_ask(List<OpenOrder> data){

        return data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase("sell")
                        && openOrder.getCumulativeQuantity() < openOrder.getQuantity())
                .map(OpenOrder::getPrice).filter(price -> price > 0)
                .reduce(Double::min).orElseThrow(()-> new DataNotFoundException("Min ask price data was not available"));

    }

    private double get_max_bid(List<OpenOrder> data){
        return data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase("buy")
                        && openOrder.getCumulativeQuantity() < openOrder.getQuantity())
                .map(OpenOrder::getPrice)
                .reduce(Double::max).orElseThrow(()-> new DataNotFoundException("Max bid price data was not available"));

    }

    private int getAvailableQuantity(List<OpenOrder> data, String side){

        return data.stream()
                .filter(openOrder -> openOrder.getSide().equalsIgnoreCase(side.equalsIgnoreCase("buy")?
                        "sell" : "buy")
                        && openOrder.getCumulativeQuantity() < openOrder.getQuantity())
                .map(openOrder -> openOrder.getQuantity() - openOrder.getCumulativeQuantity())
                .reduce(Integer::sum).orElseThrow(()-> new DataNotFoundException(" Available quantity data was not available"));
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
