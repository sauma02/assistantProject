/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;

import com.assisantsProject.asistantProject.entidades.Usuario;
import com.assisantsProject.asistantProject.repositorios.UsuarioRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class UsuarioServicio implements UserDetailsService{
@Autowired
private UsuarioRepositorio usuarioRepositorio;
    public List<Usuario> listarUsuarios() {
       return usuarioRepositorio.findAll();
    }
    public Usuario listarPorId(String id){
        Optional<Usuario> res = usuarioRepositorio.findById(id);
        if(res.isPresent()){
            Usuario us = res.get();
            return us;
        }else{
            return null;
        }
    }
    public Usuario listarPorCorreo(String correo){
        Optional<Usuario> res = usuarioRepositorio.findByCorreo(correo);
        if(res.isPresent()){
            Usuario us = res.get();
            return us;
        }else{
            return null;
        }
    }
    public Usuario registrarUsuario(Usuario usuario){
        usuarioRepositorio.save(usuario);
        return usuario;
    }
    public Usuario listarUsuarioPorUsername(String username){
        return usuarioRepositorio.findByUsuario(username);
        
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario us = usuarioRepositorio.findByUsuario(username);
        if(us != null){
            return us;
        }else{
            throw new UsernameNotFoundException("El usuario no existe");
        }
            
            
    }
    
}
