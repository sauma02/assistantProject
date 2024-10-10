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
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class UsuarioServicio {
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
    
}
