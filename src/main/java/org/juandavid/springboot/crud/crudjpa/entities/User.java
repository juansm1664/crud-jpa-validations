package org.juandavid.springboot.crud.crudjpa.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.juandavid.springboot.crud.crudjpa.validation.ExistsByUsername;

import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExistsByUsername //Anotación - Validación personalizada
     @Column(unique = true)
     @NotBlank
     @Size(min = 4, max = 20)
     private String username;

     @NotBlank
     //@JsonIgnore alternativa a @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) - No lo valida tanto escribirlo como para leerlo
     @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //No se mapea en la respuesta, solo en la petición
     private String password;

     /* Es esta sección se utiliza anotaciones JPA para definir una relación de muchos a muchos.
       Esto se lo usamos para definir la relación entre la tabla de user y la tabla de roles.
     * @ManyToMany: Se usa para definir una relación de muchos a muchos. Es decir multiples instancias de la entidad USER pueden tener multiples instancias de la entidad ROLE y viceversa.

     * @JoinTable: Esta anotación se utiliza para especificar la tabla de unión que se utiliza para mantener la relación de muchos a muchos.
        * Esta relación se maneja típicamente a través de una tercera tabla que contiene las claves foráneas de ambas relaciones.
            * name: Nombre de la tabla intermedia. -> unión de las dos tablas
            * joinColumns: Especifica la columna o columnas que se unen con la entidad que posee la asociación(en este caso,User).
                *En el ejemplo: user_id, es el nombre de la columna en la tabla de unión que se refiere a la clave primaria de la tabla de user.

            * inverseJoinColumns: Define la columna o columnas que se unen con la entidad del lado inverso de la asociación (en este caso, Role)
                * Es este ejemplo: role_id es el nombre de la columna en la tabla de unión que se refiere a la clave primaria de la tabla de Role.

            * uniqueConstraints: Esta parte es opcional y se utiliza para definir restricciones de únicas para la tabla de unión.
                * En este ejemplo, se asegura que la combinación de user_id y role_id sea única,
                 lo que significa que no se pueden insertar duplicados de la misma combinación de usuario y rol en la tabla users_roles.
     * */
     @ManyToMany
     @JoinTable(  //Tabla intermedia entre user y role usando Foreign keys
             name = "users_roles",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id"),
             uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
     )

     /* Usamos la anotación @JsonIgnore para especificar un método o campo que debe ignorarse durante los procesos de serialización y deserialización.
       A menudo aplicamos esta anotación para excluir campos que pueden no ser relevantes o que podrían contener información confidencial.
       La utilizamos en un campo o método para marcar una propiedad que deseamos ignorar.
    **/
     @JsonIgnore
     private List<Role> roles;

     /* La anotación @JsonProperty se utiliza para personalizar la serialización y deserialización de un campo o método.
            * Se puede utilizar para cambiar el nombre de un campo o método en la representación JSON.
            * Esto significa que el campo no se incluirá en la representación JSON de la entidad User.
            * La propiedad access se establece en JsonProperty.Access.READ_ONLY para indicar que el campo solo debe ignorarse durante la serialización.
            *  Esto significa que el campo se incluirá en la deserialización, lo que permite que el campo se establezca cuando se crea una instancia de la entidad User.
      */
     @JsonProperty(access = JsonProperty.Access.READ_ONLY) //No se mapea en la petición, solo en la respuesta
     private boolean enabled; //Deshabilitar usuarios para que no tenga sesión de acceso


    @PrePersist
    public void prePersist() { //Método para habilitar por defecto
        this.enabled = true;
    }

    /* La anotación @Transient para indicar que la API de persistencia de Java (JPA) debe ignorar el campo al asignar objetos a una base de datos.
       Cuando marcamos un campo con esta anotación, la JPA no conservará el campo y no recuperará su valor de la base de datos.
     */
     @Transient //No se mapea en la base de datos, es solo de la taba
     @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
     private boolean admin; //bandera para saber si es role o user

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
