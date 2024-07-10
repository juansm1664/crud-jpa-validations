package org.juandavid.springboot.crud.crudjpa.security;


import org.juandavid.springboot.crud.crudjpa.security.filter.JwtAuthenticationFilter;
import org.juandavid.springboot.crud.crudjpa.security.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {
    /*/
    En esta clase
    - se configura el passwordEncoder: que es el encargado de encriptar la contraseña de los usuarios.
    - se configura el filtro de seguridad: que es el encargado de dar permisos a las rutas de la aplicación.
    - se configura el authenticationManager: que es el encargado de autenticar a los usuarios.
    - se configura el filtro de autenticación: que es el encargado de autenticar a los usuarios.
    */
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    PasswordEncoder passwordEncoder() { //Bean(component) para encriptar la contraseña de passwordEncoder
        return new BCryptPasswordEncoder();
    }

    ;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception { //Bean para filtros de seguridad, autorización y dar permisos

        return httpSecurity.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET, "/users").permitAll() //permisos para la ruta /users
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll() //crear nuevo usuario la ruta /users
                        // .requestMatchers(HttpMethod.POST,"/users").hasRole("ADMIN") //permisos para crear solo para admin de forma programática
                        // .requestMatchers(HttpMethod.GET,"api/products","api/products{id}").hasAnyRole("ADMIN", "USER") //permisos para ver productos para admin y user

                        // .requestMatchers(HttpMethod.PUT,"api/products{id}").hasRole("ADMIN") //permisos para crear solo para admin
                        // .requestMatchers(HttpMethod.DELETE,"api/products{id}").hasRole("ADMIN") //permisos para crear solo para admin
                        .anyRequest().authenticated()) //cualquier otra ruta requiere autenticación

                .addFilter(new JwtAuthenticationFilter(authenticationManager())) //agregar filtro de autenticación
                .addFilter(new JwtValidationFilter(authenticationManager())) //agregar filtro de autenticación
                .csrf(config -> config.disable()) //autenticación básica y deshabilitar csrf(cors)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() { //Bean para configurar cors
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() { //Bean para registrar filtro de cors
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));

        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }

}