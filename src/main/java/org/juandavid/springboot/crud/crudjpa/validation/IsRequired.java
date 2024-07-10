package org.juandavid.springboot.crud.crudjpa.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RequiredValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IsRequired {

    String message() default "Este campo es requerido usando la anotación @IsRequired";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
