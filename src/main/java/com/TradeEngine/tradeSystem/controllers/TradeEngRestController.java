package com.TradeEngine.tradeSystem.controllers;

import com.TradeEngine.tradeSystem.DAOs.MarketDataRepo;
import com.TradeEngine.tradeSystem.DTOs.MarketData;
import com.TradeEngine.tradeSystem.services.TradeEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class TradeEngRestController {

    @Autowired
    MarketDataRepo marketDataRepo;

    @Autowired
    TradeEngineService tradeEngineService;

    //Endpoint that receives market data updates
    @PostMapping(value = "/md")
    public void getMarketData(@RequestBody List<MarketData> marketData){

        //Sets the new market data whenever an update occurs
        marketDataRepo.setExchange1DataRepository(marketData);


        //TODO: splitorder column false true
    }

    @PostMapping(value = "/md2")
    public void getMarketData2(@RequestBody List<MarketData> marketData){

        //Sets the new market data whenever an update occurs
        marketDataRepo.setExchange2DataRepository(marketData);


        //TODO: splitorder column false true
    }

}
