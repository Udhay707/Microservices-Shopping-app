package com.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.inventoryservice.model.Inventory;
import com.inventoryservice.repo.InventoryRepo;

@EnableEurekaClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner loadData(InventoryRepo repo) {
		
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("Lenovo Laptop");
			inventory.setQuantity(10);
			
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("Dell Laptop");
			inventory1.setQuantity(100);
			
			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("HP Laptop");
			inventory2.setQuantity(0);
			
			repo.save(inventory);
			repo.save(inventory1);
			repo.save(inventory2);
		};
	}

}
