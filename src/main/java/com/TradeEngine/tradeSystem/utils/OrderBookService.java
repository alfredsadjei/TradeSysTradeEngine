package com.TradeEngine.tradeSystem.utils;

import com.TradeEngine.tradeSystem.DTOs.OpenOrder;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.util.List;

public interface OrderBookService {

    @GET
    Call<List<OpenOrder>> getData(@Url String url);
}
