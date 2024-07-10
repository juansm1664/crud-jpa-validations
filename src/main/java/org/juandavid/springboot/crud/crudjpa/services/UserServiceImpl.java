package org.juandavid.springboot.crud.crudjpa.services;

import org.juandavid.springboot.crud.crudjpa.entities.Role;
import org.juandavid.springboot.crud.crudjpa.entities.User;
import org.juandavid.springboot.crud.crudjpa.repositories.RoleRepository;
import org.juandavid.springboot.crud.crudjpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
   private UserRepository userRepository; //Inyección del repository user

    @Autowired
    private RoleRepository roleRepository; //Inyección del repository role

    @Autowired
    private PasswordEncoder passwordEncoder; //Inyección del passwordEncoder(config security)

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }


    @Override
    @Transactional
    public User save(User user) { //método para guardar un usuario y asignarle roles

        Optional<Role> optRole = roleRepository.findByName("ROLE_USER"); //Buscar el rol por nombre

        List<Role> roles = new ArrayList<>(); //Lista vacía de roles
        optRole.ifPresent(roles::add); //Si existe el rol, lo añade a la lista

        if(user.isAdmin()){ // Comprueba si el usuario es un administrador. Si es así, busca un rol llamado "ROLE_ADMIN" en la base de datos y, si se encuentra, lo añade a la lista de roles.
            Optional<Role> optRoleAdmin = roleRepository.findByName("ROLE_ADMIN"); //Buscar el rol por nombre
            optRoleAdmin.ifPresent(roles::add);
        }
        user.setRoles(roles); //Asigna la lista de roles al usuario. Esto significa que el usuario tendrá los roles "ROLE_USER" y, si es un administrador, también "ROLE_ADMIN".
        String passwordEncoded = passwordEncoder.encode(user.getPassword()); //Codifica la contraseña del usuario utilizando el PasswordEncoder que se inyectó en la clase. Esto es para asegurar que la contraseña se almacene de forma segura en la base de datos.
        user.setPassword(passwordEncoded); //Asigna la contraseña codificada

        return userRepository.save(user); //Guarda el usuario en la base de datos
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username); //Comprueba si existe un usuario con el nombre de usuario proporcionado en la base de datos.
    }

}
