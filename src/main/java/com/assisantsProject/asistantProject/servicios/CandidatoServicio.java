/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;

import com.assisantsProject.asistantProject.entidades.Archivo;
import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.repositorios.CandidatoRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class CandidatoServicio {

    @Autowired
    private CandidatoRepositorio candidatoRepositorio;

    public Candidato registrarCandidato(Candidato candidato) {
        candidato.setEstado(true);
        candidatoRepositorio.save(candidato);
        return candidato;
    }

    public Candidato actualizarArchivos(Candidato candidato) {
        Optional<Candidato> res = candidatoRepositorio.findById(candidato.getId());
        if (res.isPresent()) {
            Candidato canEdito = res.get();
            canEdito.setArchivos(candidato.getArchivos());
            candidatoRepositorio.save(canEdito);
            return canEdito;
        } else {
            return null;
        }
    }

    public Candidato editarCandidato(Candidato candidato) {
        Optional<Candidato> res = candidatoRepositorio.findById(candidato.getId());
        if (res.isPresent()) {
            Candidato canEdit = res.get();
            canEdit.setCorreo(candidato.getCorreo());
            canEdit.setArchivos(candidato.getArchivos());
            canEdit.setEquipo(candidato.getEquipo());
            canEdit.setNombre(candidato.getNombre());
            canEdit.setFechaNacimiento(candidato.getFechaNacimiento());
            candidatoRepositorio.save(canEdit);
            return canEdit;
        } else {
            return null;
        }
    }

    public List<Archivo> mostrarArchivos(Candidato candidato) {
        return candidato.getArchivos();
    }

    public List<Candidato> listarCandidatos() {
        return candidatoRepositorio.findAll();
    }

    public Candidato listarPorCedula(String cedula) {
        Optional<Candidato> res = candidatoRepositorio.findByDoc(cedula);
        if (res.isPresent()) {
            Candidato can = res.get();
            return can;
        } else {
            return null;
        }
    }

    public Map<String, Long> obtenerPorWaveYEstado() {
        List<Candidato> candidatos = candidatoRepositorio.findAll();

        Map<String, Long> reporte = candidatos.stream()
                .collect(Collectors.groupingBy(
                        c -> String.format("Wave%s-%s", c.getWave(), c.isActivo(true) ? "Activo" : "Inactivo"), Collectors.counting()));

        return reporte;
    }

    public List<Map<String, String>> obtenerPorWaveEstado() {
        Map<String, Long> reporte = obtenerPorWaveYEstado();
        List<Map<String, String>> reporteProcesado = new ArrayList<>();

        for (Map.Entry<String, Long> entry : reporte.entrySet()) {
            String[] partes = entry.getKey().split("-");
            Map<String, String> datos = new HashMap<>();
            datos.put("wave", partes[0]);
            datos.put("estado", partes[1]);
            datos.put("cantidad", entry.getValue().toString());
            reporteProcesado.add(datos);
        }

        return reporteProcesado;
    }

    public Map<String, Long> obtenerPorEstado() {
        return candidatoRepositorio.findAll().stream()
                .collect(Collectors.groupingBy(c -> c.isActivo(true) ? "Activo" : "Inactivo",
                        Collectors.counting()));
    }
}
