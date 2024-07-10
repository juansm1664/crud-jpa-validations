package org.juandavid.springboot.crud.crudjpa.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.juandavid.springboot.crud.crudjpa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//Esta clase se utiliza para definir la lógica de validación personalizada para una anotación de restricción.
// En este caso, la anotación de restricción es ExistsByUsername.

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

    @Autowired
    private UserService userService; //Inyección del User servicio


    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

        if (userService == null){
            return true;
        }
        return !userService.existsByUsername(username);
    }
}
