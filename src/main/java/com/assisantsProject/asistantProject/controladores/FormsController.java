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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/registrarCandidato")
    public String registrarCandidatoForm(Model model, Usuario usuario) {
        model.addAttribute("listaUsuario", usuarioServicio.listarUsuarios());
        model.addAttribute("candidato", new Candidato());
        return "formularios/registrarCandidato";  // no .html extension
    }
    @PostMapping("/actualizarArchivos")
    public String actualizarArchivos(@Valid Candidato candidato, BindingResult result, 
            RedirectAttributes flash, Model model, @RequestParam("archivos[]") MultipartFile[] files){
        try {
            
        
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err -> {
                    errors.put(err.getField(),
                            "El campo".concat(err.getField()).concat("").concat(err.getDefaultMessage()));
                });
                model.addAttribute("errors", errors);
                model.addAttribute("candidato", candidato);
                return "formularios/registrarCandidato";
            }
            if (files.length == 0 || files == null || files[0].isEmpty()) {
                flash.addAttribute("clase", "danger");
                flash.addAttribute("mensaje", "No se ha cargado ningun archivo JPG, PNG, JPEG o PDF");
                return "redirect:/formularios/registrarCandidato";
            } else {
                Candidato candi = candidatoServicio.listarPorCedula(candidato.getDoc());
                Path ruta1 = Paths.get(ruta+"archivos/"+candidato.getNombre()+"/");
                Files.createDirectories(ruta1);
                List<Archivo> archivos = candi.getArchivos();
                for (MultipartFile file : files) {
                    String nombreArchivo = archivosUploads.guardarArchivo(file, this.ruta + "archivos/" + candidato.getNombre() + "/");
                    
                    

                    if ("no".equals(nombreArchivo)) {
                        flash.addAttribute("clase", "danger");
                        flash.addAttribute("mensaje", "El formato no es valido, porfavor solo suba archivos JPG, PNG, JPEG o PDF");
                        return "redirect:/formularios/registrarCandidato";
                    }
                    if (nombreArchivo != null) {
                        Archivo archivo = new Archivo();
                        archivo.setFileName(nombreArchivo);
                        archivo.setFileType(file.getContentType());
                        archivo.setCandidato(candidato);
                        archivo.setRuta(this.ruta);
                        archivoServicio.guardarArchivo(archivo);
                        archivos.add(archivo);

                    }
                }
                candidato.setArchivos(archivos);
                candidatoServicio.actualizarArchivos(candidato); // Actualizar el candidato con los archivos

                flash.addFlashAttribute("clase", "success");
                flash.addFlashAttribute("mensaje", "Éxito al registrarte y enviar archivos");
                return "redirect:/formularios/archivos_respuesta";

            }
            } catch (Exception e) {
                 e.printStackTrace();
            flash.addFlashAttribute("clase", "danger");
            flash.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/formularios/registrarCandidato";
                
        }
    }

    @PostMapping("/registrarCandidato")
    public String registrarCandidatoPost(@Valid Candidato candidato, BindingResult result,
            @RequestParam("archivos[]") MultipartFile[] files, RedirectAttributes flash, Model model) {

        try {
            
             if(candidatoServicio.listarPorCedula(candidato.getDoc())!= null){
                flash.addAttribute("clase", "danger");
                flash.addAttribute("mensaje", "Esta cedula ya se encuentra registrada, seleccione actualizar");
                return "redirect:/formularios/registrarCandidato";
             }
             if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err -> {
                    errors.put(err.getField(),
                            "El campo".concat(err.getField()).concat("").concat(err.getDefaultMessage()));
                });
                model.addAttribute("errors", errors);
                model.addAttribute("candidato", candidato);
                return "formularios/registrarCandidato";
            }
            if (files.length == 0 || files == null || files[0].isEmpty()) {
                flash.addAttribute("clase", "danger");
                flash.addAttribute("mensaje", "No se ha cargado ningun archivo JPG, PNG, JPEG o PDF");
                return "redirect:/formularios/registrarCandidato";
            } else {
                candidatoServicio.registrarCandidato(candidato);
                Path ruta1 = Paths.get(ruta+"archivos/"+candidato.getNombre()+"/");
                Files.createDirectories(ruta1);
                List<Archivo> archivos = new ArrayList<>();
                for (MultipartFile file : files) {
                    String nombreArchivo = archivosUploads.guardarArchivo(file, this.ruta + "archivos/" + candidato.getNombre() + "/");
                    
                    

                    if ("no".equals(nombreArchivo)) {
                        flash.addAttribute("clase", "danger");
                        flash.addAttribute("mensaje", "El formato no es valido, porfavor solo suba archivos JPG, PNG, JPEG o PDF");
                        return "redirect:/formularios/registrarCandidato";
                    }
                    if (nombreArchivo != null) {
                        Archivo archivo = new Archivo();
                        archivo.setFileName(nombreArchivo);
                        archivo.setFileType(file.getContentType());
                        archivo.setCandidato(candidato);
                        archivo.setRuta(this.ruta);
                        archivoServicio.guardarArchivo(archivo);
                        archivos.add(archivo);

                    }
                }
                candidato.setArchivos(archivos);
                candidatoServicio.editarCandidato(candidato); // Actualizar el candidato con los archivos

                flash.addFlashAttribute("clase", "success");
                flash.addFlashAttribute("mensaje", "Éxito al registrarte y enviar archivos");
                return "redirect:/formularios/archivos_respuesta";

            }
           

            // Asignar los archivos al candidato
        } catch (Exception e) {
            e.printStackTrace();
            flash.addFlashAttribute("clase", "danger");
            flash.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/formularios/registrarCandidato";
        }
    }
    @GetMapping("/archivos_respuesta")
    public String archivosRespuesta(Model model){
        return "archivos_respuesta";
    }

    @ModelAttribute
    public void equipo(Model model) {
        List<Usuario> listaEquipo = usuarioServicio.listarUsuarios();

        model.addAttribute("listaEquipo", listaEquipo);
    }

}
