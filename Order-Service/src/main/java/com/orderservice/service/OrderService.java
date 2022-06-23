package com.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.orderservice.dto.InventoryResponse;
import com.orderservice.dto.OrderLineItemsDto;
import com.orderservice.dto.OrderRequest;
import com.orderservice.model.Order;
import com.orderservice.model.OrderLineItems;
import com.orderservice.repository.OrderRepo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	@Autowired
	OrderRepo repo;
	
	private final WebClient.Builder webClientBuilder;
	
	@Value("${service.inventory.url}")
	String inventoryCheckUrl;
	
	public String createOrder(OrderRequest request) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineList = request.getOrderLineItemsDto().stream()
						.map(this::requestToModel)
						.toList();
		order.setOrderLineItemList(orderLineList);
		
		List<String> skuCodes = request.getOrderLineItemsDto().stream()
											.map((k)-> k.getSkuCode())
											.toList();
		
		InventoryResponse []inventoryResponses = webClientBuilder.build().get()
				.uri(inventoryCheckUrl, 
						uriBuilder -> uriBuilder.queryParam("sku-code", skuCodes)
						.build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();
	
		Boolean allProductsInStock = Arrays.stream(inventoryResponses)
									.allMatch(InventoryResponse::getIsInStock);
		
		if(allProductsInStock) {
			repo.save(order);
			return "Order Placed Successfully";
		}
		else
			throw new IllegalArgumentException("Product is not in stock");		
	}
	
	public OrderLineItems requestToModel(OrderLineItemsDto request) {
		
		OrderLineItems orderLineItems = OrderLineItems.builder()
												.skuCode(request.getSkuCode())
												.quantity(request.getQuantity())
												.price(request.getPrice())
												.build();
		return orderLineItems;
		
		
		
	}
}
