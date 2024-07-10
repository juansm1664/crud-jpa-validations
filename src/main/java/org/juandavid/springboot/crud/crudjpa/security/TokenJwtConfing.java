package org.juandavid.springboot.crud.crudjpa.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

//Clase de constantes para configurar el token JWT
public class TokenJwtConfing {


    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build(); //Clave secreta para firmar el token
    public static final String PREFIX_TOKEN = "Bearer "; //Prefijo del token JWT
    public static final String HEADER_AUTHORIZATION = "Authorization"; //Cabecera de autorización

    public static final String CONTENT_TYPE = "application/json"; //Tipo de contenido de la respuesta
}
