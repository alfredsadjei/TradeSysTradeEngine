package com.TradeEngine.tradeSystem.utils;

import com.TradeEngine.tradeSystem.DTOs.MarketData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.util.List;

public interface MarketDataService {
    @GET
    Call<List<MarketData>> getMarketData(@Url String url);

}
