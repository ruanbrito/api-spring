package com.spring.apispring.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.apispring.model.Product;
import com.spring.apispring.productDTO.ProductRecordDto;
import com.spring.apispring.repositories.ProductRepository;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {

		List<Product> p = productRepository.findAll();
		if (!p.isEmpty()) {
			for (Product prod : p) {//inserir hiperlink em cada produto
				UUID id = prod.getId();
				prod.add(linkTo(methodOn(ProductController.class).getProductId(id)).withSelfRel());
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(p);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> getProductId(@PathVariable(value = "id") UUID id) {
		Optional<Product> productId = productRepository.findById(id);
		if (productId.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(productId.get());
	}

	@PostMapping
	public ResponseEntity<Product> saveProduct(@RequestBody @Validated ProductRecordDto productRecordDto) {
		var productModel = new Product();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
		Optional<Product> prod = productRepository.findById(id);

		if (prod.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		productRepository.delete(prod.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted sucessfully.");
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
			@RequestBody @Validated ProductRecordDto productRecordDto) {
		Optional<Product> prod = productRepository.findById(id);
		if (prod.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		var product = prod.get();
		BeanUtils.copyProperties(productRecordDto, product);
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
	}

}
