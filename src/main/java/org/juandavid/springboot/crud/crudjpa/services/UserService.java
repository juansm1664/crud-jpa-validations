package org.juandavid.springboot.crud.crudjpa.services;

import org.juandavid.springboot.crud.crudjpa.entities.User;

import java.util.List;

public interface UserService {

    List<User> findAll(); //podría devolver un DTO, para manejar la información.

    User save(User user);

    boolean existsByUsername(String username);


}
