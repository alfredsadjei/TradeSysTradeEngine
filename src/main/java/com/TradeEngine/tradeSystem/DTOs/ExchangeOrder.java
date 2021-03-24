package com.TradeEngine.tradeSystem.DTOs;




public class ExchangeOrder {
    private String ticker;
    private double price;
    private int quantity;
    private String side;
    private String exchange;

    public ExchangeOrder(String ticker, double price, int quantity, String side, String exchange) {
        this.ticker = ticker;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.exchange = exchange;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return "ExchangeOrder{" +
                "ticker='" + ticker + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", side='" + side + '\'' +
                ", exchange='" + exchange + '\'' +
                '}';
    }
}
