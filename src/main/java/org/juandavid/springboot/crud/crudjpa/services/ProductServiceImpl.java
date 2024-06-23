package org.juandavid.springboot.crud.crudjpa.services;

import org.juandavid.springboot.crud.crudjpa.entities.Product;
import org.juandavid.springboot.crud.crudjpa.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id,  Product product) {
        Optional<Product> productDb = productRepository.findById(id);
        if(productDb.isPresent()){
            Product prod = productDb.orElseThrow();
            prod.setName(product.getName());
            prod.setPrice(product.getPrice());
            prod.setDescription(product.getDescription());
            return Optional.of(productRepository.save(prod));
        };
        return productDb;
    }

    @Override
    @Transactional
    public Optional<Product> delete(Long id) {
        Optional<Product> productDb = productRepository.findById(id);
        productDb.ifPresent(prod ->{productRepository.delete(prod);
        });
        return productDb;
    }
}
