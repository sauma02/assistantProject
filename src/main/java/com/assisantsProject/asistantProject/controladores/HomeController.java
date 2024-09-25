/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.controladores;


import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.servicios.CandidatoServicio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Admin
 */
@Controller
public class HomeController {
    
    @Value("${valor.ruta}")
    private String ruta;
    
    
    @Autowired
    private CandidatoServicio candidatoServicio;
    
    @GetMapping("/")
    public String home(Model model){
        return "home";
    }
    
    @GetMapping("/listaCandidatos")
    public String listaCandidatos(Model model){
        List<Candidato> listaCandidatos = candidatoServicio.listarCandidatos();
        model.addAttribute("listaCandidatos", listaCandidatos);
   
        return "listaCandidatos";
    }
    @GetMapping("/listaCandidatos/{nombre}/descargar-archivos")
    public ResponseEntity<Resource> descargarArchivosCandidatos(@PathVariable String nombre) throws IOException {
        Path directorioCandidato = Paths.get(this.ruta + "archivos/"+nombre);
        
        String nombreZip = nombre + "_archivos.zip";
        Path rutaZip = Files.createTempFile(nombreZip, ".zip");
        try(ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(rutaZip))) {
            Files.walk(directorioCandidato)
                    .filter(Files::isRegularFile)
                    .forEach(archivo -> {
                    try (InputStream in = Files.newInputStream(archivo)) {
                        ZipEntry zipEntry = new ZipEntry(directorioCandidato.relativize(archivo).toString());
                        zipOut.putNextEntry(zipEntry);
                        byte[] buffer = new byte[2000];
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                           zipOut.write(buffer, 0 ,len);
                            
                        }
                        zipOut.closeEntry();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    
                    
                    
                    });
        } 
        Resource recurso = new UrlResource(rutaZip.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreZip + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(recurso);
    }
    
}
