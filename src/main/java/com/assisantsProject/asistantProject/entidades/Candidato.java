/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.entidades;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private String nombre;
    @NotEmpty(message = "esta vacio")
    private String correo;
    @NotEmpty(message = "esta vacio")
    private String contacto;
    @NotEmpty(message = "esta vacio")
    private String tipoDoc;
    @NotEmpty(message = "esta vacio")
    private String doc;
    @NotEmpty(message = "esta vacio")
    private String fechaExpedicion;
    private String wave;
    @NotEmpty(message = "esta vacio")
    private String fechaNacimiento;
   
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Archivo> archivos;
    @ManyToOne
    @JoinColumn(name="asistente")
    private Usuario equipo;

    public Candidato() {
    }

    public Candidato(String id, String nombre, String correo, String contacto, String tipoDoc, String doc, String fechaExpedicion, String wave, String fechaNacimiento, List<Archivo> archivos, Usuario equipo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contacto = contacto;
        this.tipoDoc = tipoDoc;
        this.doc = doc;
        this.fechaExpedicion = fechaExpedicion;
        this.wave = wave;
        this.fechaNacimiento = fechaNacimiento;
        this.archivos = archivos;
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Candidato{" + "id=" + id + ", nombre=" + nombre + ", correo=" + correo + ", contacto=" + contacto + ", tipoDoc=" + tipoDoc + ", doc=" + doc + ", fechaExpedicion=" + fechaExpedicion + ", wave=" + wave + ", fechaNacimiento=" + fechaNacimiento + ", archivos=" + archivos + ", equipo=" + equipo + '}';
    }

    

  

    

    

    

   
    

}
