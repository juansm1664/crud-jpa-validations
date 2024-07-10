package org.juandavid.springboot.crud.crudjpa.security.filter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.juandavid.springboot.crud.crudjpa.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.juandavid.springboot.crud.crudjpa.security.TokenJwtConfing.*; //Importar las constantes de la configuración del token JWT

//Clase para crear la autenticación del usuario usando el filtro de autenticación de Spring Security
public class JwtAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; //Inyección de dependencia de AuthenticationManager

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
       this.authenticationManager = authenticationManager;
    }

    //Método para autenticar el usuario usando el filtro de autenticación de Spring Security
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = null; //User de la entidad User
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class); //Clase ObjectMapper de Jackson para leer el cuerpo de la solicitud-info de entrada(json)y convertirlo en un objeto User
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException | DatabindException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    //Método de autenticación para login exitoso con Spring Security

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authResult.getPrincipal(); //Obtener el usuario autenticado con Spring Security
        String username = user.getUsername(); //Obtener el nombre de usuario
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities(); //Obtener los roles del usuario autenticado

        //No poner nunca información sensible en el token JWT
        //No passwords

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles)) //Crear los claims del token JWT con los roles pasados a JSON
                .add("username", username) //Agregar el nombre de usuario a los claims
                .build();

        String token = Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 3600000)) //Establecer la fecha de expiración del token JWT en 1 hora
                .issuedAt(new Date()) //Establecer la fecha de emisión del token JWT
                .signWith(SECRET_KEY).compact(); //Generar el token JWT
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN  + token); //Agregar el token JWT a la respuesta
        Map<String, String> bodyJson = new HashMap<>();
        bodyJson.put("token", token);
        bodyJson.put("username", username);
        bodyJson.put("message", String.format("Hola %s, has iniciado sesión con éxito", username)); //Mensaje de éxito

        response.getWriter().write(new ObjectMapper().writeValueAsString(bodyJson)); //Escribir el token JWT en el cuerpo de la respuesta
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200); //Establecer el código de estado de la respuesta
    }

    //Método de autenticación para login fallido con Spring Security
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> bodyJson = new HashMap<>();
        bodyJson.put("message", "Error de autenticación: " + failed.getMessage()); //Mensaje de error sin información sensible

        response.getWriter().write(new ObjectMapper().writeValueAsString(bodyJson)); //Escribir el mensaje de error en el cuerpo de la respuesta
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401); //Establecer el código de estado de la respuesta
    }
}
