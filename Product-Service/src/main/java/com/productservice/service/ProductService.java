package com.productservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.productservice.dto.ProductRequest;
import com.productservice.dto.ProductResponse;
import com.productservice.model.Product;
import com.productservice.repository.ProductRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	
	private final ProductRepo repo;
	
	public void createProduct(ProductRequest request) {
		Product product = Product.builder()
								.name(request.getName())
								.description(request.getDescription())
								.price(request.getPrice())
								.build();
		Product save = repo.save(product);
		log.info("Product {} is saved ",product.getId());
		
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> allProducts = repo.findAll();
		
		log.info("All Product details are fetched");
		List<ProductResponse> allProductResponse = allProducts.stream()
														.map(product -> mapToProduct(product))
														.collect(Collectors.toList());
		return allProductResponse;
		
	}
	
	public ProductResponse mapToProduct(Product product) {
		
		ProductResponse productResponse = ProductResponse.builder()
												.id(product.getId())
												.name(product.getName())
												.description(product.getDescription())
												.price(product.getPrice())
												.build();
		return productResponse;
				
	
	}
}
