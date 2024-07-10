package org.juandavid.springboot.crud.crudjpa.repositories;

import org.juandavid.springboot.crud.crudjpa.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name); //Se busca por el nombre, si no lo encuentra retorna un null


}

