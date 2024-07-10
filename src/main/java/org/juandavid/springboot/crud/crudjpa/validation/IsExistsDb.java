package org.juandavid.springboot.crud.crudjpa.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsExistsDbValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsExistsDb  { //Se crea la anotación para validar si un valor ya existe en la base de datos

    String message() default "El valor ya existe en la base de datos";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
