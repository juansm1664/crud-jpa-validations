package org.juandavid.springboot.crud.crudjpa.repositories;

import org.juandavid.springboot.crud.crudjpa.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUsername (String username); //se mira si existe por el username, retorna True si lo encuentra

    Optional<User> findByUsername(String username);
}
