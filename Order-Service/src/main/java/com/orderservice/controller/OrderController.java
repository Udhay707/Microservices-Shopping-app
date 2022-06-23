package com.orderservice.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.orderservice.dto.OrderRequest;
import com.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/api/order")

public class OrderController {
	
	@Autowired
	OrderService service;
	
	@PostMapping("/place-order")
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name = "inventory", fallbackMethod = "inventoryFallBackMethod")
	@TimeLimiter(name="inventory")
	public CompletableFuture<String> placeOrder(@RequestBody OrderRequest request) {
		
		return CompletableFuture.supplyAsync(()->service.createOrder(request));		
		
	}
	
	public CompletableFuture<String> inventoryFallBackMethod(RuntimeException exception) {
		return CompletableFuture.supplyAsync(()->"Oops! Something went wrong, Please try after sometime!");
	}

}
