/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.utilidades;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Admin
 */
public class archivosUploads {

    public static String guardarArchivo(MultipartFile file, String ruta) {
        // Validar el tipo de archivo
        if (!archivosUploads.validarArchivo(file.getContentType())) {
            return "no";
        }

        // Obtener el nombre original del archivo
        String nombreOriginal = file.getOriginalFilename();
        String extension = archivosUploads.getExtension(file.getContentType());

        // Asegurarse de que el nombre del archivo no tenga extensiones duplicadas
        if (nombreOriginal != null && nombreOriginal.endsWith(extension)) {
            extension = ""; // No agregar la extensión si ya existe
        }

        // Limpiar el nombre del archivo: reemplazar espacios y caracteres especiales
        nombreOriginal = nombreOriginal.replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // Reemplazar caracteres especiales por guiones bajos

        // Crear el nombre final del archivo
        String nombre = nombreOriginal + extension.trim();

        // Eliminar posibles espacios al final de la ruta del directorio
        ruta = ruta.trim();

        // Asegurarse de que la ruta termine con un separador de directorio
        if (!ruta.endsWith(File.separator)) {
            ruta += File.separator;
        }

        // Crear el directorio si no existe
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crear los directorios necesarios
        }

        // Crear el archivo en la ruta especificada
        try {
            File imageFile = new File(ruta + nombre);

            // Transferir el archivo al disco
            file.transferTo(imageFile);
            return nombre;

        } catch (IOException e) {
            // Registrar el error y devolver el mensaje de error
            e.printStackTrace(); // Imprimir el stacktrace para debugging
            return e.getMessage();
        }
    }

    public static boolean validarArchivo(String mime) {
        boolean retorno = false;

        switch (mime) {
            case "image/jpeg":
                retorno = true;
                break;
            case "image/jpg":
                retorno = true;
                break;
            case "image/png":
                retorno = true;
                break;
            case "application/pdf":
                retorno = true;
                break;
            default:
                retorno = false;
                break;
        }
        return retorno;
    }

    public static String getExtension(String mime) {
        String retorno = "";
        switch (mime) {
            case "image/jpeg":
                retorno = ".jpg";
                break;
            case "image/jpg":
                retorno = ".jpg";
                break;
            case "image/png":
                retorno = ".png";
                break;
            case "application/pdf":
                retorno = ".pdf";
                break;
            default:
                retorno = "Error, archivo no admitido";
                break;

        }
        return retorno;
    }
}
