package com.TradeEngine.tradeSystem.DTOs;


//This represents the order being received by the engine
public class ProductOrder {

    private String id; //random UUID string;
    private String productName; //product name
    private double price; //how much client is willing to buy or sell for
    private Long clientId;
    private double funds;
    private int quantityOwned;
    private int quantity; // number of products to buy or sell
    private String side; // buy or sell order
    private String date;
    private String status;

    public String getID() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityOwned() {
        return quantityOwned;
    }

    public void setQuantityOwned(int quantityOwned) {
        this.quantityOwned = quantityOwned;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
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

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", clientId=" + clientId +
                ", funds=" + funds +
                ", quantityOwned=" + quantityOwned +
                ", quantity=" + quantity +
                ", side='" + side + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
