package com.TradeEngine.tradeSystem.utils;

public enum RedisServer {
    SERVER_KEY("EWbpysz7KwsBbFo4xXBvQ5qx2IwWKlOH");

    private final String KeyVal;

    RedisServer(String key) {
        this.KeyVal = key;
    }

    public String getKeyVal(){
        return this.KeyVal;
    }
}
