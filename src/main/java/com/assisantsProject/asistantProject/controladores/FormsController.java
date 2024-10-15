/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.controladores;

import com.assisantsProject.asistantProject.entidades.Archivo;
import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.entidades.Rol;
import com.assisantsProject.asistantProject.entidades.Usuario;
import com.assisantsProject.asistantProject.repositorios.ArchivoRepositorio;
import com.assisantsProject.asistantProject.servicios.ArchivoServicio;
import com.assisantsProject.asistantProject.servicios.CandidatoServicio;
import com.assisantsProject.asistantProject.servicios.RolServicio;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private RolServicio rolServicio;

    @Value("${valor.ruta}")
    private String ruta;

    @GetMapping("/editarCandidato/{id}")
    public String editarCandidato(@PathVariable("id") String id, Model model) {
        Candidato can = candidatoServicio.listarPorId(id);
        model.addAttribute("candidato", can);

        return "formularios/editarCandidato";
    }

    @GetMapping("/registrarUsuario")
    public String registrarCandidato(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("rol", rolServicio.listaRoles());
        return "registrarUsuario";

    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuarioForm(@Valid Usuario usuario, BindingResult result, RedirectAttributes flash, Model model) {
        try {

            if (usuarioServicio.listarPorCorreo(usuario.getCorreo()) != null) {
                flash.addFlashAttribute("clase", "danger");
                flash.addFlashAttribute("mensaje", "Este correo ya se encuentra registrado");
                return "redirect:/formularios/registrarUsuario";
            }
            usuarioServicio.registrarUsuario(usuario);
            flash.addFlashAttribute("clase", "success");
            flash.addFlashAttribute("mensaje", "Exito al registrar");
            return "redirect:/formularios/registrarUsuario";

        } catch (Exception e) {
            e.printStackTrace();
            flash.addFlashAttribute("clase", "danger");
            flash.addFlashAttribute("mensaje", e.getCause());
            return "redirect:/formularios/registrarUsuario";
        }
    }

    @PostMapping("/editarCandidato")
    public String editarCandidatoForm(@RequestParam("id") String id, @RequestParam("nombre") String nombre, @RequestParam("correo") String correo, @RequestParam("contacto") String contacto,
            @RequestParam("tipoDoc") String tipoDoc, @RequestParam("doc") String doc, @RequestParam("fechaExpedicion") String fechaExpedicion,
            @RequestParam("wave") String wave, @RequestParam("fechaNacimiento") String fechaNacimiento, @RequestParam("equipo") String equipoId,
            RedirectAttributes flash, Model model, @RequestParam("archivos[]") MultipartFile[] files) {
        try {
            Candidato can = candidatoServicio.listarPorId(id);
            Usuario u = usuarioServicio.listarPorId(equipoId);
            can.setNombre(nombre);
            can.setCorreo(correo);
            can.setContacto(contacto);
            can.setTipoDoc(tipoDoc);
            can.setDoc(doc);
            can.setFechaExpedicion(fechaExpedicion);
            can.setWave(wave);
            can.setEquipo(u);
            can.setFechaNacimiento(fechaNacimiento);

            // Check if there are files to upload
            if (files != null && files.length > 0 && !files[0].isEmpty()) {
                List<Archivo> archivos = can.getArchivos() != null ? can.getArchivos() : new ArrayList<>();

                // Loop through each file and process it
                for (MultipartFile file : files) {
                    String nombreArchivo = archivosUploads.guardarArchivo(file, this.ruta + "archivos/" + can.getNombre().trim() + "/");

                    if ("no".equals(nombreArchivo)) {
                        flash.addFlashAttribute("clase", "danger");
                        flash.addFlashAttribute("mensaje", "El formato no es válido. Por favor, suba solo archivos JPG, PNG, JPEG o PDF.");
                        return "redirect:/formularios/editarCandidato/" + can.getId();
                    }

                    // If file saved successfully, create an Archivo object and associate it with the candidate
                    if (nombreArchivo != null) {
                        Archivo archivo = new Archivo();
                        archivo.setFileName(nombreArchivo);
                        archivo.setFileType(file.getContentType());
                        archivo.setCandidato(can);
                        archivo.setRuta(this.ruta);

                        archivoServicio.guardarArchivo(archivo);
                        archivos.add(archivo);
                    }
                }

                // Set the candidate's files and update the candidate
                can.setArchivos(archivos);
            }

            // Update the candidate regardless of file upload
            candidatoServicio.editarCandidato(can);

            flash.addFlashAttribute("clase", "success");
            flash.addFlashAttribute("mensaje", "Candidato editado con éxito");
            return "redirect:/formularios/editarCandidato/" + can.getId();

        } catch (Exception e) {
            Candidato can = candidatoServicio.listarPorId(id);
            flash.addFlashAttribute("clase", "danger");
            flash.addFlashAttribute("mensaje", "Error al editar candidato. Por favor intente de nuevo.");
            return "redirect:/formularios/editarCandidato/" + can.getId();
        }
    }

    @GetMapping("/registrarCandidato")
    public String registrarCandidatoForm(Model model, Usuario usuario) {
        model.addAttribute("listaUsuario", usuarioServicio.listarUsuarios());
        model.addAttribute("candidato", new Candidato());
        return "formularios/registrarCandidato";  // no .html extension
    }

    @PostMapping("/actualizarArchivos")
    public String actualizarArchivos(@Valid Candidato candidato, BindingResult result,
            RedirectAttributes flash, Model model, @RequestParam("archivos[]") MultipartFile[] files) {
        try {
            if (files == null || files.length == 0 || files[0].isEmpty()) {
                flash.addFlashAttribute("clase", "danger");
                flash.addFlashAttribute("mensaje", "No se ha cargado ningún archivo JPG, PNG, JPEG o PDF");
                return "redirect:/formularios/registrarCandidato";
            } else {
                // Verificar si el candidato ya existe en la base de datos
                Candidato candi = candidatoServicio.listarPorCedula(candidato.getDoc());
                if (candi == null) {
                    // Si no existe, guardarlo primero
                    candi = candidatoServicio.registrarCandidato(candidato);
                }

                List<Archivo> archivos = candi.getArchivos();
                String rutaArchivo = this.ruta + "archivos/" + candi.getNombre() + "/";

                for (MultipartFile file : files) {
                    String nombreArchivo = archivosUploads.guardarArchivo(file, rutaArchivo);

                    if ("no".equals(nombreArchivo)) {
                        flash.addFlashAttribute("clase", "danger");
                        flash.addFlashAttribute("mensaje", "El formato no es válido, por favor solo suba archivos JPG, PNG, JPEG o PDF");
                        return "redirect:/formularios/registrarCandidato";
                    }
                    if (nombreArchivo != null) {
                        Archivo archivo = new Archivo();
                        archivo.setFileName(nombreArchivo);
                        archivo.setFileType(file.getContentType());
                        archivo.setCandidato(candi); // Asignar el candidato persistido
                        archivo.setRuta(this.ruta);
                        archivoServicio.guardarArchivo(archivo);
                        archivos.add(archivo);
                    }
                }

                // Actualizar los archivos del candidato persistido
                candi.setArchivos(archivos);
                candidatoServicio.actualizarArchivos(candi);

                flash.addFlashAttribute("clase", "success");
                flash.addFlashAttribute("mensaje", "Éxito al actualizar los archivos");
                return "redirect:/formularios/archivos_respuesta";
            }
        } catch (Exception e) {
            flash.addFlashAttribute("clase", "danger");
            flash.addFlashAttribute("mensaje", "Error al actualizar los archivos. Por favor intente de nuevo.");
            return "redirect:/formularios/registrarCandidato";
        }
    }

    @PostMapping("/registrarCandidato")
    public String registrarCandidatoPost(@Valid Candidato candidato, BindingResult result,
            @RequestParam("archivos[]") MultipartFile[] files, RedirectAttributes flash, Model model) {

        try {
            // Validar si la cédula ya está registrada
            if (candidatoServicio.listarPorCedula(candidato.getDoc()) != null) {
                flash.addFlashAttribute("clase", "danger");
                flash.addFlashAttribute("mensaje", "Esta cédula ya se encuentra registrada, seleccione actualizar.");
                return "redirect:/formularios/registrarCandidato";
            }

            // Validar errores en el formulario
//            if (result.hasErrors()) {
//                Map<String, String> errors = new HashMap<>();
//                result.getFieldErrors().forEach(err -> {
//                    errors.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
//                });
//                model.addAttribute("errors", errors);
//                model.addAttribute("candidato", candidato);
//                return "formularios/registrarCandidato";
//            }
            // Validar archivos
            if (files == null || files.length == 0 || files[0].isEmpty()) {
                flash.addFlashAttribute("clase", "danger");
                flash.addFlashAttribute("mensaje", "No se ha cargado ningún archivo JPG, PNG, JPEG o PDF.");
                return "redirect:/formularios/registrarCandidato";
            }

            // Registrar candidato en la base de datos
            candidatoServicio.registrarCandidato(candidato);

            // Crear directorio para guardar archivos
            Path ruta1 = Paths.get(ruta + "archivos/" + candidato.getNombre().trim() + "/");
            Files.createDirectories(ruta1);

            // Lista para almacenar los archivos procesados
            List<Archivo> archivos = new ArrayList<>();
            for (MultipartFile file : files) {
                // Guardar el archivo en el sistema de archivos
                String nombreArchivo = archivosUploads.guardarArchivo(file, this.ruta + "archivos/" + candidato.getNombre() + "/");

                // Verificar si el formato de archivo es válido
                if ("no".equals(nombreArchivo)) {
                    flash.addFlashAttribute("clase", "danger");
                    flash.addFlashAttribute("mensaje", "El formato no es válido. Por favor, suba solo archivos JPG, PNG, JPEG o PDF.");
                    return "redirect:/formularios/registrarCandidato";
                }

                // Crear objeto Archivo y asociarlo al candidato
                if (nombreArchivo != null) {
                    Archivo archivo = new Archivo();
                    archivo.setFileName(nombreArchivo);
                    archivo.setFileType(file.getContentType());
                    archivo.setCandidato(candidato);
                    archivo.setRuta(this.ruta);

                    // Guardar el archivo en la base de datos
                    archivoServicio.guardarArchivo(archivo);
                    archivos.add(archivo);
                }
            }

            // Asignar archivos al candidato y actualizarlo
            candidato.setArchivos(archivos);
            candidatoServicio.editarCandidato(candidato);

            flash.addFlashAttribute("clase", "success");
            flash.addFlashAttribute("mensaje", "Éxito al registrarte y enviar archivos.");
            return "redirect:/formularios/archivos_respuesta";

        } catch (Exception e) {
            e.printStackTrace();
            flash.addFlashAttribute("clase", "danger");
            flash.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/formularios/registrarCandidato";
        }
    }

    @GetMapping("/archivos_respuesta")
    public String archivosRespuesta(Model model) {
        return "archivos_respuesta";
    }

    @ModelAttribute
    public void equipo(Model model) {
        List<Usuario> listaEquipo = usuarioServicio.listarUsuarios();

        model.addAttribute("listaEquipo", listaEquipo);
    }
    @ModelAttribute
    public void rol(Model model){
        List<Rol> listarRoles = rolServicio.listaRoles();
        
        model.addAttribute("listaRol", listarRoles);
    }

}
