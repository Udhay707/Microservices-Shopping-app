package com.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.productservice.model.Product;

public interface ProductRepo extends MongoRepository<Product, String> {

}
