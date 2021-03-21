package com.TradeEngine.tradeSystem.DAOs;

import com.TradeEngine.tradeSystem.DTOs.MarketData;

import java.util.List;

public class MarketDataRepo {
    private List<MarketData> dataRepository;

    public MarketDataRepo(){ }

    public MarketDataRepo(List<MarketData> dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<MarketData> getDataRepository() {
        return dataRepository;
    }

    public void setDataRepository(List<MarketData> dataRepository) {
        this.dataRepository = dataRepository;
    }
}
