package org.juandavid.springboot.crud.crudjpa.controllers;

import jakarta.validation.Valid;
import org.juandavid.springboot.crud.crudjpa.entities.Product;
import org.juandavid.springboot.crud.crudjpa.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200") //Permitir peticiones desde el frontend
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService serviceProduct;


    @PreAuthorize("hasAnyRole('ADMIN','USER')") //permisos para listar para admin y user (dinamico) de spring security
    @GetMapping
    public List<Product> list(){
        return serviceProduct.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> viewProduct(@PathVariable Long id ){
        Optional<Product> productOpt = serviceProduct.findById(id);
        if(productOpt.isPresent()){
            return ResponseEntity.ok(productOpt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    /*
    Este método es un ejemplo de cómo implementar la creación de recursos en una API REST utilizando Spring Boot.
    Con validación de entrada y control de acceso basado en roles.
    @PreAuthorize("hasRole('ADMIN')") //permisos para crear solo para admin, es mas harcodeado, no sirve mucho para obtener los datos de la base de datos, no sirve para dinamico
     @PostMapping //permisos para crear solo para admin, se usa para crear nuevos recursos en el servidor
        @Valid -> activa la validación de Spring sobre el objeto Product basada en las anotaciones de validación presentes en la clase Product
        @RequestBody -> indica que el objeto Product se obtiene del cuerpo de la petición HTTP
        BindingResult -> captura los errores de validación que puedan surgir al validar el objeto Product
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping //permisos para crear solo para admin, se usa para crear nuevos recursos en el servidor
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceProduct.save(product));
    }
    /*
    Este método es un ejemplo de cómo implementar la actualización de recursos en una API REST utilizando Spring Boot.
    Con validación de entrada y control de acceso basado en roles.
    @PUTMapping("/{id}") -> método para modificar algún recurso para manejarlas peticiones HTTP PUT a la URL /api/products/{id}
        bindingResult.hasFieldErrors() devuelve true, significa que hay al menos un error de validación en los campos del objeto Product,
        como un campo faltante que es requerido o un valor que no cumple con las restricciones definidas.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct (@Valid @RequestBody Product product, @PathVariable Long id, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){ //if para manejar errores de validación
            return validation(bindingResult);
        }
        /* serviceProduct.update(id, product);: Esta línea llama al método update del servicio ProductService,
           pasando como argumentos el id del producto que se desea actualizar y el objeto product que contiene
           los nuevos datos del producto.
           El método update devuelve un Optional<Product>.
           Optional es una clase de contenedor que puede contener o no un valor. En este contexto, se utiliza para manejar la posibilidad de que el producto con el id especificado no exista en la base de datos.
         */
        Optional<Product> productOptional = serviceProduct.update(id, product);
        if(productOptional.isPresent()){
        return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());

        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
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

     private ResponseEntity<?> validation(BindingResult bindingResult) {
         Map<String, String> errors = new HashMap<>();
         bindingResult.getFieldErrors().forEach(err ->{
             errors.put(err.getField(),"El campo " + err.getField() + " "+ err.getField());
         });
         return ResponseEntity.badRequest().body(errors);
    }

}
