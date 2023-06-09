package com.asseco.blog.tcsample.service;

import com.asseco.blog.tcsample.dto.ProductDto;
import com.asseco.blog.tcsample.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final NbpRatesClient ratesClient;
    public ProductDto getProduct(String id) {
        var product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .currency(product.getCurrency())
                .price(product.getPrice())
                .pricePLN("PLN".equals(product.getCurrency()) ? product.getPrice() : calculatePLN(product.getCurrency(), product.getPrice()))
                .build();
    }

    private BigDecimal calculatePLN(String currency, BigDecimal price) {
        var nbpRate = ratesClient.getRate("A", currency);
        var rate = nbpRate.getRates().get(0).getMid();
        return price.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_EVEN);
    }
}
