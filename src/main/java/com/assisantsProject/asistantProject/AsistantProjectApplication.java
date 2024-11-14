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

    @Bean
    public CommandLineRunner commandLineRunner(UsuarioRepositorio usuarioRepositorio, RolRepositorio rolRepositorio, PasswordEncoder passwordEncoder) {
        return args -> {
            Usuario usuario = usuarioRepositorio.findByUsuario("sebastian.henao");
            Usuario usuarioAdmin = usuarioRepositorio.findByUsuario("gloria.gonzales");
            Rol rolUser = rolRepositorio.findFirstByNombre("usuario");
            Rol rolAdmin = rolRepositorio.findFirstByNombre("admin");
            if (rolUser != null && usuario != null) {
                System.out.println("Usuario ya existe");
            } else if(rolUser != null){
              
                Usuario user = new Usuario(null, "sebastian.henao", passwordEncoder.encode("12345"), "sebastian.fernandezhenao@iqor.com", rolUser, null);
                usuarioRepositorio.save(user);

            } else{
                Rol us = new Rol();
                us.setNombre("usuario");
                us.setDescripcion("Supervisor");
                rolRepositorio.save(us);
                Usuario user = new Usuario(null, "sebastian.henao", passwordEncoder.encode("12345"), "sebastian.fernandezhenao@iqor.com", us, null);
                usuarioRepositorio.save(user);
            }
            if (usuarioAdmin != null && rolAdmin != null) {
                System.out.println("Usuario admin ya existe");
            } else if(usuarioAdmin != null){
               
                
                Usuario admin = new Usuario(null, "gloria.gonzales", passwordEncoder.encode("12345"), "gloria.gonzales@iqor.com", rolAdmin, null);
                usuarioRepositorio.save(admin);
            }else{
                Rol ad = new Rol();
                ad.setNombre("admin");
                ad.setDescripcion("Administrador");
                rolRepositorio.save(ad);
                Usuario admin = new Usuario(null, "gloria.gonzales", passwordEncoder.encode("12345"), "gloria.gonzales@iqor.com", rolAdmin, null);
                usuarioRepositorio.save(admin);
            }
        };

    }
}
