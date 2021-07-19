package com.assignment.warehouse.db.mapper;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.entity.ProductSubArticleEntity;
import com.assignment.warehouse.model.Product;
import com.assignment.warehouse.model.ProductSubArticle;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductEntityToProductConverter implements Converter<ProductEntity, Product> {

    @NonNull
    private final Converter<ProductSubArticleEntity, ProductSubArticle> productSubArticleEntityProductSubArticleConverter;

    @Override
    public Product convert(ProductEntity productEntity) {

        var product = new Product();
        product.setName(productEntity.getName());
        product.setPrice(productEntity.getPrice());

        var parts = productEntity.getSubArticles()
                .stream()
                .map(productSubArticleEntityProductSubArticleConverter::convert)
                .collect(Collectors.toList());

        product.setParts(parts);
        return product;
    }
}
