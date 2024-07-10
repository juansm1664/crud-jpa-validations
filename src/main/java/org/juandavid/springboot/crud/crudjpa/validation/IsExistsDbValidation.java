package org.juandavid.springboot.crud.crudjpa.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.juandavid.springboot.crud.crudjpa.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

public class IsExistsDbValidation implements ConstraintValidator<IsExistsDb, String> {

    @Autowired
    private ProductService productService;

    @Override
    public boolean isValid(String value , ConstraintValidatorContext context) {
        if (productService != null) {
            return !productService.existsBySku(value);
        }
       return  true;
    }
}
