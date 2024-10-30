/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.controladores;

import com.assisantsProject.asistantProject.config.CustomSuccessHandler;
import com.assisantsProject.asistantProject.entidades.Candidato;
import com.assisantsProject.asistantProject.entidades.Usuario;
import com.assisantsProject.asistantProject.entidades.Wave;
import com.assisantsProject.asistantProject.servicios.CandidatoServicio;
import com.assisantsProject.asistantProject.servicios.ExcelServicio;
import com.assisantsProject.asistantProject.servicios.UsuarioServicio;
import com.assisantsProject.asistantProject.servicios.WaveServicio;
import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.angus.mail.iap.Response;
import static org.hibernate.internal.CoreLogging.logger;
import static org.hibernate.internal.HEMLogging.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.integration.mail.dsl.Mail.headers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.web.servlet.function.RequestPredicates.headers;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
@Controller
public class HomeController {

    @Value("${valor.ruta}")
    private String ruta;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    
    @Autowired
    private CandidatoServicio candidatoServicio;

    @Autowired
    private ExcelServicio excelServicio;

    @Autowired
    private WaveServicio waveServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomSuccessHandler authenticationSuccessHandler;

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/registrarWave")
    @ResponseBody // Ensures response is serialized into JSON
    public ResponseEntity<Map<String, String>> registrarWaveForm(@Valid Wave wave, BindingResult result) {
        Map<String, String> response = new HashMap<>();

        try {

            if (waveServicio.listarWavePorNombre(wave.getNombre()) != null) {
                response.put("clase", "error");
                response.put("mensaje", "Esta wave ya se encuentra registrada");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Return conflict if wave exists
            }

            waveServicio.registrarWave(wave);
            response.put("clase", "success");
            response.put("mensaje", "Exito al registrar wave");
            return ResponseEntity.ok(response); // Return success response

        } catch (Exception e) {
            response.put("clase", "error");
            response.put("mensaje", "Ocurrió un error al registrar la wave");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return server error
        }
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes flash, ModelMap map) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Usuario user = usuarioServicio.listarUsuarioPorUsername(username);
            if (user.getPassword().equals(password)) {
                // Authenticate the user
                Authentication authentication = authenticationManager.authenticate(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Determine the redirect URL using the authentication success handler
                String redirectUrl = authenticationSuccessHandler.determineTargetUrlForAuthentication(authentication);

                // Redirect the user to the determined URL
                return "redirect:" + redirectUrl;
            } else {
                flash.addAttribute("class", "danger");
                flash.addAttribute("mensaje", "Usuario y/o contraseña incorrecto");

                return "redirect:/login";
            }

        } catch (AuthenticationException e) {
            e.printStackTrace();
            if (e.getCause() != null) {
                System.err.println("Error: " + e.getCause().toString());
            }
            return "error.html";
        }

    }

    @GetMapping("/listaCandidatos")
    public String listaCandidatos(Model model) {
        List<Candidato> listaCandidatos = candidatoServicio.listarCandidatos();

        model.addAttribute("listaCandidatos", listaCandidatos);

        return "listaCandidatos";
    }

    @GetMapping("/eliminarCandidato/{id}")
    public ResponseEntity<Map<String, String>> eliminarCandidato(@PathVariable("id") String id) {
        Map<String, String> response = new HashMap<>();
        try {
            Candidato can = candidatoServicio.listarPorId(id);
            candidatoServicio.eliminarCandidato(can);
            response.put("clase", "success");
            response.put("mensaje", "Éxito al eliminar");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("clase", "error");
            response.put("mensaje", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cambiarEstado/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> cambiarEstado(@PathVariable("id") String id) {
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println(id);
            Candidato can = candidatoServicio.listarPorId(id);
            if (can.isEstado()) {
                can.setEstado(false);
            } else {
                can.setEstado(true);
            }
            candidatoServicio.editarCandidato(can);
            response.put("clase", "success");
            response.put("mensaje", "Éxito al cambiar estado");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("clase", "error");
            response.put("mensaje", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/candidatos")
    public ResponseEntity<InputStreamResource> descargarExcel() {
        List<Candidato> lista = candidatoServicio.listarCandidatos();

        ByteArrayInputStream bis = excelServicio.exportCandidatosToExcel(lista);

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=candidatos.xlsx");
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/listaCandidatos/{nombre}/descargar-archivos")
    public ResponseEntity<Resource> descargarArchivosCandidatos(@PathVariable String nombre) throws IOException {
        // Retain the original name with spaces, just sanitize invalid characters
        String nombreNormalizado = nombre.replaceAll("[^a-zA-Z0-9\\-\\.\\_\\s]", "").trim(); // Allow spaces
        Path directorioCandidato = Paths.get(this.ruta + "archivos/" + nombreNormalizado);

        // Verificar si el directorio existe
        if (!Files.exists(directorioCandidato) || !Files.isDirectory(directorioCandidato)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El directorio no existe");
        }

        String nombreZip = nombreNormalizado + "_archivos.zip"; // Use the name with spaces if present
        Path rutaZip = Files.createTempFile(null, ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(rutaZip))) {
            Files.walk(directorioCandidato)
                    .filter(Files::isRegularFile)
                    .forEach(archivo -> {
                        try (InputStream in = Files.newInputStream(archivo)) {
                            ZipEntry zipEntry = new ZipEntry(directorioCandidato.relativize(archivo).toString());
                            zipOut.putNextEntry(zipEntry);
                            byte[] buffer = new byte[4096];
                            int len;
                            while ((len = in.read(buffer)) > 0) {
                                zipOut.write(buffer, 0, len);
                            }
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace(); // Handle exceptions properly
                        }
                    });
        }

        Resource recurso = new UrlResource(rutaZip.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreZip + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(recurso);
    }

    @GetMapping("/asignarWave/{id}")
    public ResponseEntity<Map<String, String>> asignarWave(@PathVariable("id") String id, @RequestParam("wave") String wave) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validate wave
            if (wave == null || wave.isEmpty()) {
                response.put("clase", "error");
                response.put("mensaje", "Debe seleccionar una wave válida.");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate candidate ID
            Candidato can = candidatoServicio.listarPorId(id);
            if (can == null) {
                response.put("clase", "error");
                response.put("mensaje", "Candidato no encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Assign wave
            System.out.println(wave);
            Wave wa = waveServicio.listarWavePorId(wave);
            System.out.println(wa);
            if (wa == null) {
                response.put("clase", "error");
                response.put("mensaje", "Wave no encontrada.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            can.setWave(wa);
            candidatoServicio.editarCandidato(can);

            response.put("clase", "success");
            response.put("mensaje", "Éxito al cambiar wave");
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("clase", "error");
            response.put("mensaje", "Error en la búsqueda de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Use a logging framework instead of printStackTrace
            logger.error("Error asignando wave: ", e);
            response.put("clase", "error");
            response.put("mensaje", "Ocurrió un error inesperado.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @ModelAttribute
    public void wave(Model model) {
        model.addAttribute("wave", new Wave());
    }

    @ModelAttribute
    public void listaWave(Model model) {
        model.addAttribute("listaWave", waveServicio.listarWaves());

    }
}
