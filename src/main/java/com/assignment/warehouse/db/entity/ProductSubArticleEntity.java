package com.assignment.warehouse.db.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_composition")
public class ProductSubArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "article_quantity")
    private String articleQuantity;
}
