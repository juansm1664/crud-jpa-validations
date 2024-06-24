package org.juandavid.springboot.crud.crudjpa.services;

import org.juandavid.springboot.crud.crudjpa.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);


    Optional<Product> update(Long id, Product product);

    //Optional<Product> delete(Product product);
    Optional<Product> delete(Long id);
}


// findAll -> Buscar todos los productos, es una lista y es opcional porque puede
// Optional -> Es unn objeto contenedor que puede o no contener un valor no nulo. Si hay un valor presente, isPresent() devuelve true. Si no hay ningún valor, el objeto se considera vacío y isPresent() devuelve false.
//Se proporcionan métodos adicionales que dependen de la presencia o ausencia de un valor contenido, como orElse() (devuelve un valor por defecto si no hay ningún valor presente) e ifPresent() (realiza una acción si hay un valor presente).
//findById -> Buscar por id los productos, contiene el objeto contenedor de opcional
//save -> Guardar los productos
//update -> Actualizar los productos, con PathVariable para el id y la entidad Producto
//delete -> Eliminar los productos, con PathVariable para el id y la entidad Producto


