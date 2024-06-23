package org.juandavid.springboot.crud.crudjpa.repositories;

import org.juandavid.springboot.crud.crudjpa.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
