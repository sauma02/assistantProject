package com.assisantsProject.asistantProject;

import com.assisantsProject.asistantProject.entidades.Rol;
import com.assisantsProject.asistantProject.entidades.Usuario;
import com.assisantsProject.asistantProject.repositorios.RolRepositorio;
import com.assisantsProject.asistantProject.repositorios.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AsistantProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsistantProjectApplication.class, args);
    }

    

    }

