package org.juandavid.springboot.crud.crudjpa.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{NotEmpty.product.name}") //No permite atributo vacío
    @Size(min=4, max=20) //define el tamaño del atributo
    private String name;

    @Min(100) // valor numeric mín requerido
    @NotNull //No permite valores nulos
    private Integer price;

    @NotBlank(message = "{NotBlank.product.description}") //No permite atributo vacío, Ni espacio en blanco
    private String description;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
