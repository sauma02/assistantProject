/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.repositorios;

import com.assisantsProject.asistantProject.entidades.Candidato;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface CandidatoRepositorio extends JpaRepository<Candidato, String>  {
    public Optional<Candidato> findByDoc(String cedula);
    
}
