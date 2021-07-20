package com.assignment.warehouse.controller;

import com.assignment.warehouse.model.Article;
import com.assignment.warehouse.model.Inventory;
import com.assignment.warehouse.service.StockService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/stock")
@AllArgsConstructor
public class StockController {

    @NonNull
    private final StockService stockService;

    @GetMapping(value = "/articles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Inventory getAllArticles() {
        return stockService.findAll();
    }

    @GetMapping(value = "/articles/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Article getArticle(@PathVariable final String id) {
        return stockService.findOne(id);
    }

    @DeleteMapping(value = "/articles/{id}")
    public void deleteArticle(@PathVariable final String id) {
        stockService.deleteOne(id);
    }
}
