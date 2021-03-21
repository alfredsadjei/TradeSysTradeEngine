package com.TradeEngine.tradeSystem;

import com.TradeEngine.tradeSystem.services.TradeEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeSystemApplication.class, args);
	}

}
