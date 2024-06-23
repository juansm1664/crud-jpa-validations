package org.juandavid.springboot.crud.crudjpa.controllers;

import org.juandavid.springboot.crud.crudjpa.entities.Product;
import org.juandavid.springboot.crud.crudjpa.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService serviceProduct;


    @GetMapping
    public List<Product> list(){
        return serviceProduct.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewProduct(@PathVariable Long id ){
        Optional<Product> productOpt = serviceProduct.findById(id);
        if(productOpt.isPresent()){
            return ResponseEntity.ok(productOpt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceProduct.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@RequestBody Product product){
        Optional<Product> productOptional = serviceProduct.update(id, product);
        if(productOptional.isPresent()){
        return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id ){
       // Product product = new Product();
       // product.setId(id);
       // Optional<Product> productOpt = serviceProduct.delete(product);
        Optional<Product> productOpt = serviceProduct.delete(id);
        if(productOpt.isPresent()){
            return ResponseEntity.ok(productOpt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

}
