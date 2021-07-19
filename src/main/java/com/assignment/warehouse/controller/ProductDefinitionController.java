package com.assignment.warehouse.controller;

import com.assignment.warehouse.model.Product;
import com.assignment.warehouse.model.Products;
import com.assignment.warehouse.service.ProductDefinitionService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/definition")
@AllArgsConstructor
public class ProductDefinitionController {
    @NonNull
    private final ProductDefinitionService productDefinitionService;

    @GetMapping(value = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Products getAllProducts() {
        return productDefinitionService.findAll();
    }

    @GetMapping(value = "/products/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable String name) {
        return productDefinitionService.findOne(name);
    }

    @DeleteMapping(value = "/products/{name}")
    public void deleteProduct(@PathVariable String name) {
        productDefinitionService.deleteOne(name);
    }
}
