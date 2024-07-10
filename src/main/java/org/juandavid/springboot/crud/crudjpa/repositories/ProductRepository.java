package org.juandavid.springboot.crud.crudjpa.repositories;

import org.juandavid.springboot.crud.crudjpa.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    boolean existsBySku(String sku); //Se busca por el sku, si no lo encuentra retorna un true o false

}
