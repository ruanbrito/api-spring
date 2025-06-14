package com.spring.apispring.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.apispring.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

}
