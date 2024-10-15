/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.repositorios;

import com.assisantsProject.asistantProject.entidades.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    public Optional<Usuario> findByCorreo(String correo);
    public Usuario findByUsuario(String usuario);
}
