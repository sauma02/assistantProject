/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.config;

import com.assisantsProject.asistantProject.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Admin
 */
@Configuration
public class webConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Value("${valor.ruta}")
    private String ruta;
    @Value("${valor.form}")
    private String form;
    



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/formularios/**").addResourceLocations("file: "+this.form);
        registry.addResourceHandler("/uploads/**").addResourceLocations("file: " + this.ruta);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            return http.formLogin(form -> form
                    .loginPage("/login")
                    .failureUrl("/login")
                    .successHandler(myAuthenticationSuccessHandler())
                    .permitAll()
            )
                    .authorizeHttpRequests(res -> res
                    // Publicly accessible endpoints
                    .requestMatchers("/login", "/formularios/registrarCandidato").permitAll()
                    .requestMatchers("/static/**", "/js/**", "/css/**", "/images/**").permitAll()
                    // POST access for specific roles
                    .requestMatchers(HttpMethod.POST, "/asignarWave/**").hasAnyRole("admin", "usuario", "asistente")
                    // Admin and user authority only
                    .requestMatchers("/**", "/formularios/registrarUsuario",
                            "/formularios/editarCandidato", "/formularios/registrarWave")
                    .hasAnyAuthority("admin", "usuario")
                    // All other requests require authentication
                    .anyRequest().authenticated()
                    )
                    .userDetailsService(usuarioServicio)
                    .build();
        } catch (Exception e) {
            throw new Exception("Error at: " + e.getMessage(), e);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authentication -> authentication;
    }

    @Bean
    //Se crea el bean nuevo para poder redireccionar basado en el rol de la persona
    public CustomSuccessHandler myAuthenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }

    //https://www.baeldung.com/spring-redirect-after-login
}
