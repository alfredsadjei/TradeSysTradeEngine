package com.TradeEngine.tradeSystem.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenOrder {

    private String product;
    private int quantity;
    private double price;
    private String side;
    private List<Execution> executions;
    private int cumulativeQuantity;

    public OpenOrder(String product, int quantity, double price, String side, List<Execution> executions, int cumulativeQuantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.executions = executions;
        this.cumulativeQuantity = cumulativeQuantity;
    }

    public String getProductName() {
        return product;
    }

    public void setProductName(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public List<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Execution> executions) {
        this.executions = executions;
    }

    public int getCumulativeQuantity() {
        return cumulativeQuantity;
    }

    public void setCumulativeQuantity(int cumulativeQuantity) {
        this.cumulativeQuantity = cumulativeQuantity;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Execution {
        private String timestamp;
        private double price;
        private int quantity;

        public Execution() {

        }

        public Execution(String timestamp, double price, int quantity) {
            this.timestamp = timestamp;
            this.price = price;
            this.quantity = quantity;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
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

        @Override
        public String toString() {
            return "Execution{" +
                    "timestamp='" + timestamp + '\'' +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OpenOrder{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side='" + side + '\'' +
                ", executions=" + executions +
                ", cumulativeQuantity=" + cumulativeQuantity +
                '}';
    }
}
