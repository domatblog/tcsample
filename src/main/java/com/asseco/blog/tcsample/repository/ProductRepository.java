package com.asseco.blog.tcsample.repository;

import com.asseco.blog.tcsample.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
