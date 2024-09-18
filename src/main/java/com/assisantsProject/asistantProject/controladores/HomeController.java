/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.controladores;


import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.servicios.CandidatoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Admin
 */
@Controller
public class HomeController {
    
    @Autowired
    private CandidatoServicio candidatoServicio;
    
    @GetMapping("/listaCandidatos")
    public String listaCandidatos(Model model){
        List<Candidato> listaCandidatos = candidatoServicio.listarCandidatos();
        model.addAttribute("listaCandidatos", listaCandidatos);
        return "listaCandidatos";
    }
    
}
