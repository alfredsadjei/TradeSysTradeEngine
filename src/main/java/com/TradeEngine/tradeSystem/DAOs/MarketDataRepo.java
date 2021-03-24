package com.TradeEngine.tradeSystem.DAOs;

import com.TradeEngine.tradeSystem.DTOs.MarketData;
import com.TradeEngine.tradeSystem.utils.MarketDataService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class MarketDataRepo implements ApplicationRunner {
    private List<MarketData> exchange1DataRepository;
    private List<MarketData> exchange2DataRepository;

    public MarketDataRepo(){ }


    public List<MarketData> getExchange1DataRepository() {
        return exchange1DataRepository;
    }

    public void setExchange1DataRepository(List<MarketData> exchange1DataRepository) {
        this.exchange1DataRepository = exchange1DataRepository;
    }

    public List<MarketData> getExchange2DataRepository() {
        return exchange2DataRepository;
    }

    public void setExchange2DataRepository(List<MarketData> exchange2DataRepository) {
        this.exchange2DataRepository = exchange2DataRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://exchange.matraining.com/md/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        MarketDataService mdService = retrofit.create(MarketDataService.class);

        Call<List<MarketData>> md1 = mdService.getMarketData("https://exchange.matraining.com/md");
        Call<List<MarketData>> md2 = mdService.getMarketData("https://exchange2.matraining.com/md");



        exchange1DataRepository = md1.execute().body();
        exchange2DataRepository = md2.execute().body();


    }
}
