/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Value("${valor.ruta}")
    private String ruta;
    
    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry){
        registry.addResourceHandler("/uploads/**").addResourceLocations("file: "+this.ruta);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        try {
            return http.formLogin(form -> form
            .loginPage("/login")
            .permitAll()        
            )
            .authorizeHttpRequests(res -> res
            .requestMatchers("/**", "/static/**", "/js/**", "/css/**", 
                    "/images/**", "/login/**", "/templates/**")
            .permitAll()
            .anyRequest().authenticated()
            ).build();
        } catch (Exception e) {
            throw new Exception("Error at: "+e.getCause());
        }
    }
}
