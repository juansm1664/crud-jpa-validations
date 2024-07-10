package org.juandavid.springboot.crud.crudjpa.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//Clase abstracta para crear un SimpleGrantedAuthority con JsonCreator
//Es un mixIn para mezclar la clase SimpleGrantedAuthority con SimpleGrantedAuthorityJsonCreator para q pueda aceptar authority en vez de rol
public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator //Anotación para que pueda crear un objeto SimpleGrantedAuthority
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {

    }
}
