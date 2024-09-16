/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;

import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.repositorios.CandidatoRepositorio;
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
}
