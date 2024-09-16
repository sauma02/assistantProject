/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Admin
 */
@Entity
@Getter
@Setter
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String Nombre;
    private String correo;
    private String fechaNacimiento;
    @OneToMany
    private List<Archivo> archivos;
    @ManyToOne
    private List<Usuario> equipo;
    

    public Candidato() {
    }

    public Candidato(String id, String Nombre, String correo, String fechaNacimiento, List<Archivo> archivos, List<Usuario> equipo) {
        this.id = id;
        this.Nombre = Nombre;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.archivos = archivos;
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Candidato{" + "id=" + id + ", Nombre=" + Nombre + ", correo=" + correo + ", fechaNacimiento=" + fechaNacimiento + ", archivos=" + archivos + ", equipo=" + equipo + '}';
    }
    
    
    
    
    
}
