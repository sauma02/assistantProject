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
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "esta vacio")
    private String Nombre;
    @NotEmpty(message = "esta vacio")
    private String correo;
    @NotEmpty(message = "esta vacio")
    private String contacto;

    private String wave;
    @NotEmpty(message = "esta vacio")
    private String fechaNacimiento;
    @NotEmpty(message = "esta vacio")
    @OneToMany
    private List<Archivo> archivos;
    @ManyToOne
    private Usuario equipo;

    public Candidato() {
    }

    public Candidato(String id, String Nombre, String correo, String contacto, String wave, String fechaNacimiento, List<Archivo> archivos, Usuario equipo) {
        this.id = id;
        this.Nombre = Nombre;
        this.correo = correo;
        this.contacto = contacto;
        this.wave = wave;
        this.fechaNacimiento = fechaNacimiento;
        this.archivos = archivos;
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Candidato{" + "id=" + id + ", Nombre=" + Nombre + ", correo=" + correo + ", contacto=" + contacto + ", fechaNacimiento=" + fechaNacimiento + ", archivos=" + archivos + ", equipo=" + equipo + '}';
    }

}
