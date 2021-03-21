package com.TradeEngine.tradeSystem.DTOs;

public class MarketData {

    private String TICKER;
    private int    SELL_LIMIT;
    private double LAST_TRADED_PRICE;
    private double BID_PRICE;
    private double MAX_PRICE_SHIFT;
    private double ASK_PRICE;
    private double BUY_LIMIT;

    public MarketData(double LAST_TRADED_PRICE, double BID_PRICE, int SELL_LIMIT, double MAX_PRICE_SHIFT, String TICKER, double ASK_PRICE, double BUY_LIMIT) {
        this.LAST_TRADED_PRICE = LAST_TRADED_PRICE;
        this.BID_PRICE = BID_PRICE;
        this.SELL_LIMIT = SELL_LIMIT;
        this.MAX_PRICE_SHIFT = MAX_PRICE_SHIFT;
        this.TICKER = TICKER;
        this.ASK_PRICE = ASK_PRICE;
        this.BUY_LIMIT = BUY_LIMIT;
    }


    public double getLAST_TRADED_PRICE() {
        return LAST_TRADED_PRICE;
    }

    public void setLAST_TRADED_PRICE(double LAST_TRADED_PRICE) {
        this.LAST_TRADED_PRICE = LAST_TRADED_PRICE;
    }

    public double getBID_PRICE() {
        return BID_PRICE;
    }

    public void setBID_PRICE(double BID_PRICE) {
        this.BID_PRICE = BID_PRICE;
    }

    public int getSELL_LIMIT() {
        return SELL_LIMIT;
    }

    public void setSELL_LIMIT(int SELL_LIMIT) {
        this.SELL_LIMIT = SELL_LIMIT;
    }

    public double getMAX_PRICE_SHIFT() {
        return MAX_PRICE_SHIFT;
    }

    public void setMAX_PRICE_SHIFT(double MAX_PRICE_SHIFT) {
        this.MAX_PRICE_SHIFT = MAX_PRICE_SHIFT;
    }

    public String getTICKER() {
        return TICKER;
    }

    public void setTICKER(String TICKER) {
        this.TICKER = TICKER;
    }

    public double getASK_PRICE() {
        return ASK_PRICE;
    }

    public void setASK_PRICE(double ASK_PRICE) {
        this.ASK_PRICE = ASK_PRICE;
    }

    public double getBUY_LIMIT() {
        return BUY_LIMIT;
    }

    public void setBUY_LIMIT(double BUY_LIMIT) {
        this.BUY_LIMIT = BUY_LIMIT;
    }

    @Override
    public String toString() {
        return "MarketData{" +
                "LAST_TRADED_PRICE=" + LAST_TRADED_PRICE +
                ", BID_PRICE=" + BID_PRICE +
                ", SELL_LIMIT=" + SELL_LIMIT +
                ", MAX_PRICE_SHIFT=" + MAX_PRICE_SHIFT +
                ", TICKER='" + TICKER + '\'' +
                ", ASK_PRICE=" + ASK_PRICE +
                ", BUY_LIMIT=" + BUY_LIMIT +
                '}';
    }
}

