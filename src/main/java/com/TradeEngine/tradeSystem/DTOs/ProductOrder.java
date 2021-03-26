package com.TradeEngine.tradeSystem.DTOs;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//This represents the order being received by the engine
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrder {
    private String productName; //product name
    private double price; //how much client is willing to buy or sell for
    private int quantity; // number of products to buy or sell
    private String side; // buy or sell order
    private String status;
    private String exchange;

    public ProductOrder(String productName, double price, int quantity, String side, String exchange) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.exchange = exchange;
    }

    public ProductOrder() {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", side='" + side + '\'' +
                ", status='" + status + '\'' +
                ", exchange='" + exchange + '\'' +
                '}';
    }
}
