/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Admin
 */
@Entity

@Getter
@Setter
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String fileName;
    private String fileType;
    private String ruta;
     @ManyToOne
    @JoinColumn(name = "candidato")
    private Candidato candidato;

    public Archivo() {
    }

    public Archivo(String id, String fileName, String fileType, String ruta, Candidato candidato) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.ruta = ruta;
        this.candidato = candidato;
    }
    

    @Override
    public String toString() {
        return "Archivo{" + "id=" + id + ", fileName=" + fileName + ", fileType=" + fileType + ", ruta=" + ruta + ", candidato=" + candidato + '}';
    }

    

    

}
