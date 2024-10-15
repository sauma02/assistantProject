/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.assisantsProject.asistantProject.repositorios;

import com.assisantsProject.asistantProject.entidades.Wave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USUARIO
 */
@Repository
public interface WaveRepositorio extends JpaRepository<Wave, String> {
    
}
