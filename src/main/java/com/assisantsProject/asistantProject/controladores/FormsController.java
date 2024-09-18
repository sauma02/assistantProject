/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.controladores;

import com.assisantsProject.asistantProject.entidades.Archivo;
import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.entidades.Usuario;
import com.assisantsProject.asistantProject.repositorios.ArchivoRepositorio;
import com.assisantsProject.asistantProject.servicios.ArchivoServicio;
import com.assisantsProject.asistantProject.servicios.CandidatoServicio;
import com.assisantsProject.asistantProject.servicios.UsuarioServicio;
import com.assisantsProject.asistantProject.utilidades.archivosUploads;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/formularios")
public class FormsController {
    @Autowired
    private CandidatoServicio candidatoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ArchivoServicio archivoServicio;
    
    @Value("${valor.ruta}")
    private String ruta;
    
    @GetMapping("/formularios/registrarCandidato")
    public String registrarCandidatoForm(Model model, Usuario usuario){
        model.addAttribute("listaUsuario", usuarioServicio.listarUsuarios());
        model.addAttribute("candidato", new Candidato());
        return "formularios/registrarCandidato";
    }
    @PostMapping("/formularios/registrarCandidato")
    public String registrarCandidatoPost(@Valid Candidato candidato, BindingResult result,@RequestParam("archivos[]") MultipartFile file [],
    RedirectAttributes flash, Model model){
        try {
            if(result.hasErrors()){
               Map<String, String> errors = new HashMap<>(); 
               result.getFieldErrors().forEach(err -> {
               errors.put(err.getField(), 
                       "El campo".concat(err.getField()).concat("").concat(err.getDefaultMessage()));
               });
               model.addAttribute("errors", errors);
               model.addAttribute("candidato", candidato);
               return "formularios/registrarCandidato";
            }
            if(file == null){
                flash.addAttribute("clase", "danger");
                flash.addAttribute("mensaje", "No se han cargado archivos");
                return "redirect:/formularios/registrarCandidato";
            }else{
                
                List<Archivo> archivos = new ArrayList<>();
                for (MultipartFile fi : file) {
                    
                    String nombreImagen = archivosUploads.guardarArchivo(fi, this.ruta + "archivos/");
                    if(nombreImagen == "no"){
                       flash.addAttribute("clase", "danger");
                       flash.addAttribute("mensaje", "El formato ingresado no es admitido");
                       return "redirect:/formularios/registrarCandidato";
                    }
                    if(nombreImagen != null){
                        Archivo archivo = new Archivo();
                        archivo.setFileName(nombreImagen);
                        archivo.setFileType(fi.getContentType());
                        archivo.setRuta(ruta);
                        archivos.add(archivo);
                        
                    }
                }
                candidato.setArchivos(archivos);
                candidatoServicio.registrarCandidato(candidato);
                
                
            }
            flash.addAttribute("clase", "success");
            flash.addAttribute("mensaje", "Exito al registrate y enviar archivos");
            return "/archivos_respuesta";
        } catch (Exception e) {
            e.printStackTrace();
            flash.addAttribute("clase", "success" );
            flash.addAttribute("mensaje", e.getMessage());
            return "redirect/formularios/registrarCandidato";
        }
 
    }
    
}
