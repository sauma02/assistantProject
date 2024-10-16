/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;

import com.assisantsProject.asistantProject.entidades.Rol;
import com.assisantsProject.asistantProject.repositorios.RolRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class RolServicio {
    @Autowired
    private RolRepositorio rolRepositorio;
    
    public List<Rol> listaRoles(){
        return rolRepositorio.findAll();
    }
    public Rol listarPorId(String rol){
        Optional<Rol> res = rolRepositorio.findById(rol);
        if(res.isPresent()){
            Rol role = res.get();
            return role;
        }else{
            return null;
        }
    }
}
