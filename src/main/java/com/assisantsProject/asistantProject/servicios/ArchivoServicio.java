/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;

import com.assisantsProject.asistantProject.entidades.Archivo;
import com.assisantsProject.asistantProject.repositorios.ArchivoRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ArchivoServicio {
    @Autowired
    private ArchivoRepositorio archivoRepositorio;
    
    public Archivo guardarArchivo(Archivo archivo){
        archivoRepositorio.save(archivo);
        return archivo;
    }
    public void eliminarArchivos(List<Archivo> archivos){
        for (Archivo archivo : archivos) {
            archivoRepositorio.delete(archivo);
        }
    }
}
