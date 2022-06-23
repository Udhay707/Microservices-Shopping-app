package com.inventoryservice.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventoryservice.model.Inventory;


@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {

	public List<Inventory> findBySkuCodeIn(List<String> skuCode);

}
