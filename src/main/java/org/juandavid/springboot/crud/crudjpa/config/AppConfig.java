package org.juandavid.springboot.crud.crudjpa.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties") //Config los mensajes de validación de la entidad
public class AppConfig {
}
