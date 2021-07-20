package com.assignment.warehouse.db.mapper;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.entity.ProductSubArticleEntity;
import com.assignment.warehouse.model.AvailableProduct;
import com.assignment.warehouse.model.ProductSubArticle;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductEntityToAvailableProductConverter implements Converter<ProductEntity, AvailableProduct> {

    @NonNull
    private final Converter<ProductSubArticleEntity, ProductSubArticle> productSubArticleEntityProductSubArticleConverter;

    @Override
    public AvailableProduct convert(final ProductEntity productEntity) {

        var product = new AvailableProduct();
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
