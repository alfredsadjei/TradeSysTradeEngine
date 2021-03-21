package com.TradeEngine.tradeSystem.config;

import com.TradeEngine.tradeSystem.DAOs.MarketDataRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Creating a bean to store market data received from the exchanges
@Configuration
public class MarketDataStorage {

    //TODO: populate list with initial market data
    // since marketData generated on exchange update
    // List<MarketData> initData;

    @Bean
    public MarketDataRepo getMarketDataRepo(){
        return new MarketDataRepo();
    }

}
