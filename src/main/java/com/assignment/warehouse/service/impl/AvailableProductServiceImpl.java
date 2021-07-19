package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.assignment.warehouse.exception.StockNotAvailable;
import com.assignment.warehouse.model.AvailableProduct;
import com.assignment.warehouse.model.PurchaseProduct;
import com.assignment.warehouse.model.PurchaseResponse;
import com.assignment.warehouse.service.AvailableProductService;
import com.assignment.warehouse.service.AvailableQuantityService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class AvailableProductServiceImpl implements AvailableProductService {

    private static final long ZERO = 0l;
    private static final String ZERO_STR = "0";

    @NonNull
    private final ProductRepository productRepository;

    @NonNull
    private final AvailableQuantityService quantityService;

    @NonNull
    private final Converter<ProductEntity, AvailableProduct> productEntityAvailableProductConverter;

    @Override
    public List<AvailableProduct> findAll() {
        var products = productRepository.findAll()
                .stream()
                .map(productEntityAvailableProductConverter::convert)
                .filter(Objects::nonNull)
                .map(this::updateQuantity)
                .filter(product -> product.getQuantity() > ZERO)
                .collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new StockNotAvailable("No product available");
        }
        return products;
    }

    @Override
    public PurchaseResponse sell(List<PurchaseProduct> items) {

        var total = new BigDecimal(ZERO_STR);
        Map<String, Long> articlesNeeded = new HashMap<>();
        for (var item : items) {
            var product = getProduct(item.getName());

            product.getParts().forEach(article -> {
                var key = article.getArticleId();
                long quantityNeeded = Long.parseLong(article.getArticleCount()) * item.getQuantity() + articlesNeeded.getOrDefault(key, ZERO);
                articlesNeeded.put(key, quantityNeeded);
            });

            total = total.add(getPrice(item, product));
        }

        quantityService.updateStock(articlesNeeded);

        var response = new PurchaseResponse();
        response.setTotalAmount(total);
        return response;
    }

    private AvailableProduct getProduct(String name) {
        return productRepository.findByName(name)
                .map(productEntityAvailableProductConverter::convert)
                .orElseThrow(() -> new StockNotAvailable(String.format("Product '%s' doesn't exists", name)));
    }

    private BigDecimal getPrice(PurchaseProduct item, AvailableProduct product) {
        var price = Optional.ofNullable(product.getPrice()).orElse(ZERO_STR);
        var quantity = new BigDecimal(item.getQuantity());

        return new BigDecimal(price).multiply(quantity);
    }

    private AvailableProduct updateQuantity(AvailableProduct product) {
        product.setQuantity(quantityService.calculate(product.getParts()));
        return product;
    }
}
