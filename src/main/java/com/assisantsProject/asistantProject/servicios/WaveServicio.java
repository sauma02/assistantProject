/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;

import com.assisantsProject.asistantProject.entidades.Wave;
import com.assisantsProject.asistantProject.repositorios.WaveRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author USUARIO
 */
@Service
public class WaveServicio {
    @Autowired
    private WaveRepositorio waveRepositorio;
    
    public Wave registrarWave(Wave wave){
        waveRepositorio.save(wave);
        return wave;
    }
    public List<Wave> listarWaves(){
        return waveRepositorio.findAll();
    }
}
