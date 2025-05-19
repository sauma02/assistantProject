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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Admin
 */
@Entity
@Getter
@Setter
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotEmpty(message = "esta vacio")
    private String usuario;
    @NotEmpty(message = "esta vacio")
    private String password;
    @NotEmpty(message = "esta vacio")
    private String correo;
    @OneToOne
    @JoinColumn(name = "rol")
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        try {
            List<GrantedAuthority> authorityList = new ArrayList();
            List<Rol> role = new ArrayList();
            role.add(rol);
            for (Rol rol : role) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rol.getNombre());
                authorityList.add(authority);

            }
            return authorityList;
        } catch (Exception e) {
            e.printStackTrace();

            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            return null;
        }

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.usuario;
    }

    @Override
    public String getPassword() {
     return null;
    }

}
