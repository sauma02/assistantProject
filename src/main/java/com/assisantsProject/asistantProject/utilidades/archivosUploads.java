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
    public static String guardarArchivo(MultipartFile file, String ruta){
        if(archivosUploads.validarArchivo(file.getContentType()) == false){
          return "no";  
        }else{
            
            String nombre = file.getOriginalFilename() + archivosUploads.getExtension(file.getContentType());
            try {
                File imageFile = new File(ruta+nombre);
                
                file.transferTo(imageFile);
                return nombre;
            } catch (IOException e) {
                System.err.println(e.getStackTrace());
                
                return e.getMessage();
                
            }
        }
    }
  
    public static boolean validarArchivo(String mime){
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
    public static String getExtension(String mime){
        String retorno = "";
        switch(mime){
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
