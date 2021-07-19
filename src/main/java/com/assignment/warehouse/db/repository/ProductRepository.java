package com.assignment.warehouse.db.repository;

import com.assignment.warehouse.db.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findByName(String name);
}
