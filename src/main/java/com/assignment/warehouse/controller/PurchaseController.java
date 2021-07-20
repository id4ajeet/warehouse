package com.assignment.warehouse.controller;

import com.assignment.warehouse.model.AvailableProduct;
import com.assignment.warehouse.model.PurchaseProduct;
import com.assignment.warehouse.model.PurchaseResponse;
import com.assignment.warehouse.service.AvailableProductService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/purchase")
@AllArgsConstructor
public class PurchaseController {

    @NonNull
    private final AvailableProductService availableProductService;

    @GetMapping(value = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AvailableProduct> getAllProducts() {
        return availableProductService.findAll();
    }

    @PostMapping(value = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public PurchaseResponse updateProducts(@RequestBody final List<PurchaseProduct> request) {
        return availableProductService.sell(request);
    }
}
