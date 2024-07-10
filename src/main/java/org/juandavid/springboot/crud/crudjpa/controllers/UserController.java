package org.juandavid.springboot.crud.crudjpa.controllers;

import jakarta.validation.Valid;
import org.juandavid.springboot.crud.crudjpa.entities.User;
import org.juandavid.springboot.crud.crudjpa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Controlador de usuarios
    *Se encarga de manejar las peticiones relacionadas con los usuarios
    * @CrossOrigin(origins = "http://localhost:4200") Permite peticiones desde el frontend
    * @RestController Indica que es un controlador REST
    * @RequestMapping("/users") Indica que las peticiones a este controlador se harán en la ruta /users
 */

@CrossOrigin(origins = "http://localhost:4200") //Permitir peticiones desde el frontend
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private  UserService userService; //Inyección del USer servicio

    @GetMapping
    public List<User> list(){
        return userService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')") //método privado para crear admin: true
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){ //if para manejar errores de validación
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));

    }
    @PostMapping("/register") //método para registrar un usuario reutilizando el método create
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult){
        user.setAdmin(false);
        return create(user, bindingResult);
    }

    //Método para registrar usuarios pero sin reutilizar método
    //@PostMapping("/register")
    //public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult){
    //    if(bindingResult.hasFieldErrors()){ //if para manejar errores de validación
    //        return validation(bindingResult);
    //    }
    //    user.setAdmin(false);
    //    return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    //}

    private ResponseEntity<?> validation(BindingResult bindingResult) { //Método para manejar errores de validación
         Map<String, String> errors = new HashMap<>();
         bindingResult.getFieldErrors().forEach(err ->{
             errors.put(err.getField(),"El campo " + err.getField() + " "+ err.getDefaultMessage());
         });
         return ResponseEntity.badRequest().body(errors);
    }

}
