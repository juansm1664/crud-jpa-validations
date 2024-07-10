package org.juandavid.springboot.crud.crudjpa.services;

import org.juandavid.springboot.crud.crudjpa.entities.User;
import org.juandavid.springboot.crud.crudjpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Se puede con cualquier nombre, se puede con cualquier fuente de datos (mongoDB...)
/*Esta clase JpaUSerDetailsService implementa la interfaz UserDetailsService de Spring Security, que es un componente clave en el proceso de autenticación de Spring Security.
    Su propósito principal es cargar los detalles de un usuario dado por su nombre de usuario durante el proceso de autenticación
 */
@Service
public class JpaUSerDetailsService implements UserDetailsService {

    //Clase para validar el login de los usuarios, está en el contexto de spring security
    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Este método se sobrescribe de la interfaz UserDetailsService. Se llama cuando Spring Security necesita cargar los detalles de un usuario por su nombre de usuario.
        Optional<User> userOptional = userRepository.findByUsername(username); //Se busca el usuario por el nombre en el repositorio

        if (userOptional.isEmpty()){ //> Comprueba sí está vacío, es decir, si no se encontró un usuario con el nombre de usuario dado. Si es así, se lanza una excepción UsernameNotFoundException.
            throw new UsernameNotFoundException(String.format("Username %S NO existe en el sistema", username));
        }
        User user = userOptional.orElseThrow(); //Se obtiene el usuario

        List<GrantedAuthority> authorities = user.getRoles() //se obtienen los roles del usuario y los convierte en un obj de una lista de GrantedAuthority (Esta es una interfaz de Spring Security)
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        //Esta línea crea y devuelve un nuevo objeto User de Spring Security con los detalles del usuario.
        // Este objeto User se utilizará para la autenticación y autorización en Spring Security.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // Se encripta con Encryp y Se compara con el usuario de la base de datos
                user.getPassword(), //Se compara con el pass de la base de datos
                user.isEnabled(), //Se compara con el estado de la base de datos
                true,
                true,
                true, authorities);
    }
}
