package com.inventoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventoryservice.dto.InventoryResponse;
import com.inventoryservice.repo.InventoryRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {

	@Autowired
	private InventoryRepo repo;

	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		
		log.info("Stock check for {} sku",skuCode);
		
		//mimic timeout in circuit-breaker
		log.warn("Timeout Started");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.warn("Timeout Ended");
		
		List<InventoryResponse> response = repo.findBySkuCodeIn(skuCode).stream()
		 								.map(inventory -> {
		 									return InventoryResponse.builder()
		 									.skuCode(inventory.getSkuCode())
		 									.isInStock(inventory.getQuantity() > 0)
		 									.build();
		 								})
		 								.toList();
		
		return response;
				
		
	}

}
