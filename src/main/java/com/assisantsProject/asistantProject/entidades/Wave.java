/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Embedded.Empty;

/**
 *
 * @author USUARIO
 */
@Entity
@Getter
@Setter
public class Wave {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotEmpty(message="esta vacio")
    private String nombre;
    @OneToMany
    private List<Candidato> candidato;

    public Wave() {
    }

    public Wave(String id, String nombre, List<Candidato> candidato) {
        this.id = id;
        this.nombre = nombre;
        this.candidato = candidato;
    }
    
    
            
}
