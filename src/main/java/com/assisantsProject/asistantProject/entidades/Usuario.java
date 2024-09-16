/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String usuario;
    private String password;
    private String correo;
    private Rol rol;
    @OneToMany
    private List<Candidato> candidatos;

    public Usuario() {
    }

    public Usuario(String id, String usuario, String password, String correo, Rol rol, List<Candidato> candidatos) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.correo = correo;
        this.rol = rol;
        this.candidatos = candidatos;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", usuario=" + usuario + ", password=" + password + ", correo=" + correo + ", rol=" + rol + ", candidatos=" + candidatos + '}';
    }
    
    
    
    
}
