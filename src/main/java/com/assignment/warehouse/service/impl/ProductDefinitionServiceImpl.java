package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.assignment.warehouse.exception.ProductNotFound;
import com.assignment.warehouse.model.Product;
import com.assignment.warehouse.model.Products;
import com.assignment.warehouse.service.ProductDefinitionService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class ProductDefinitionServiceImpl implements ProductDefinitionService {

    @NonNull
    private ProductRepository productRepository;

    @NonNull
    private Converter<ProductEntity, Product> productEntityProductConverter;

    @Override
    public Products findAll() {
        final var items = productRepository.findAll()
                .stream()
                .map(productEntityProductConverter::convert)
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            throw new ProductNotFound("No product present in warehouse");
        }

        var products = new Products();
        products.setItems(items);
        return products;
    }

    @Override
    public Product findOne(final String name) {
        return productRepository.findByName(name)
                .map(productEntityProductConverter::convert)
                .orElseThrow(() -> new ProductNotFound(String.format("Product[%s] not found", name)));
    }

    @Override
    @Transactional
    public void deleteOne(final String name) {
        productRepository.findByName(name)
                .ifPresent(productRepository::delete);
    }
}
