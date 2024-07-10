package org.juandavid.springboot.crud.crudjpa.validation;


import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Esta anotación se encarga de validar si un usuario existe en la base de datos
//Anotación personalizada

@Constraint(validatedBy = ExistsByUsernameValidation.class) //Se encarga de validar con la clase ExistsByUsernameValidation
@Retention(RetentionPolicy.RUNTIME) //Se ejecuta en tiempo de ejecución
@Target(ElementType.FIELD) //Se aplica a un campo
public @interface ExistsByUsername {

        String message() default "YA existe en la base de datos"; //Mensaje por defecto

        Class<?>[] groups() default {}; //Grupos de validación

        Class<?>[] payload() default {}; //Carga de validación

}
