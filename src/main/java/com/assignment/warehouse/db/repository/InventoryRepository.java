package com.assignment.warehouse.db.repository;

import com.assignment.warehouse.db.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<ArticleEntity, String> {
}
