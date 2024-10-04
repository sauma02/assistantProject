/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;

import com.assisantsProject.asistantProject.entidades.Archivo;
import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.repositorios.CandidatoRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class CandidatoServicio {
    @Autowired
    private CandidatoRepositorio candidatoRepositorio;
    
    public Candidato registrarCandidato(Candidato candidato){
        candidatoRepositorio.save(candidato);
        return candidato;
    }
    public Candidato actualizarArchivos(Candidato candidato){
        Optional<Candidato> res = candidatoRepositorio.findById(candidato.getId());
        if(res.isPresent()){
            Candidato canEdito = res.get();
            canEdito.setArchivos(candidato.getArchivos());
            candidatoRepositorio.save(canEdito);
            return canEdito;
        }else{
            return null;
        }
    }
    public Candidato editarCandidato(Candidato candidato){
       Optional<Candidato> res =  candidatoRepositorio.findById(candidato.getId());
       if(res.isPresent()){
           Candidato canEdit = res.get();
           canEdit.setCorreo(candidato.getCorreo());
           canEdit.setArchivos(candidato.getArchivos());
           canEdit.setEquipo(candidato.getEquipo());
           canEdit.setNombre(candidato.getNombre());
           canEdit.setFechaNacimiento(candidato.getFechaNacimiento());
           candidatoRepositorio.save(canEdit);
           return canEdit;
       }else{
            return null;
       }
    }
    public List<Archivo> mostrarArchivos(Candidato candidato){
        return candidato.getArchivos();
    }
    public List<Candidato> listarCandidatos(){
        return candidatoRepositorio.findAll();
    }
    public Candidato listarPorCedula(String cedula){
        Optional<Candidato> res = candidatoRepositorio.findByDoc(cedula);
        if(res.isPresent()){
            Candidato can = res.get();
            return can;
        }else{
            return null;
        }
    }
}
