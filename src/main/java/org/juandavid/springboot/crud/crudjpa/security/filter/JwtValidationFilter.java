package org.juandavid.springboot.crud.crudjpa.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.juandavid.springboot.crud.crudjpa.security.SimpleGrantedAuthorityJsonCreator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.juandavid.springboot.crud.crudjpa.security.TokenJwtConfing.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {


    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        if(header == null || !header.startsWith(PREFIX_TOKEN)){ //Valida que sea distinto de null
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace(PREFIX_TOKEN, ""); //Extraer el token de la cabecera bearer

        try {
            Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build().parseSignedClaims(token) //parsear el token
                .getPayload();
            String username = claims.getSubject(); //obtener el nombre de usuario
            Object  authoritiesClaims = claims.get("authorities"); //obtener los roles - authorities que viene de authentication filter

            //Es un arreglo porque un usuario puede tener varios roles
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class) //mezclar la clase SimpleGrantedAuthority que viene con ROLES a  la clase SimpleGrantedAuthorityJsonCreator que permite con authority
                            .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)); //convertir las autoridades en una colección de GrantedAuthority

            // Convertir el String de autoridades a una lista de GrantedAuthority
            //List<GrantedAuthority> authorityList = Arrays.stream(authoritiesClaims.split(","))
            //        .map(SimpleGrantedAuthority::new)
            //        .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken); //autenticar el usuario
            chain.doFilter(request, response); //continuar con la cadena de filtros

        } catch (JwtException e){
            Map<String, String> bodyError = new HashMap<>();
            bodyError.put("error", e.getMessage());
            bodyError.put("message", "Token no válido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(bodyError));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

    }
}
