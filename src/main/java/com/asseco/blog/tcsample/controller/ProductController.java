package com.asseco.blog.tcsample.controller;

import com.asseco.blog.tcsample.dto.ProductDto;
import com.asseco.blog.tcsample.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ProductDto getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }
}
