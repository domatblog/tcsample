package com.asseco.blog.tcsample.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {
    private String name;
    private String description;
    private String currency;
    private BigDecimal price;
    private BigDecimal pricePLN;
}
