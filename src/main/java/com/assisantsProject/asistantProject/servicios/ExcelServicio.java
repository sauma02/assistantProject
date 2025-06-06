/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.servicios;
import com.assisantsProject.asistantProject.entidades.Candidato;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
/**
 *
 * @author USUARIO
 */
@Service
public class ExcelServicio {
  
    public ByteArrayInputStream exportCandidatosToExcel(List<Candidato> candidatos) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Crear la hoja de Excel
            Sheet sheet = workbook.createSheet("Candidatos");

            // Encabezado
            String[] header = {"Nombre", "Correo", "Tipo de documento", "Numero de documento", "Fecha de expedicion", "Fecha de nacimiento",
                    "Estado","BlackListed", "Wave"};
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(headerStyle);
            }

            // Datos de los candidatos
            int rowIdx = 1;
            for (Candidato candidato : candidatos) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(candidato.getNombre());
                row.createCell(1).setCellValue(candidato.getCorreo());
                row.createCell(2).setCellValue(candidato.getTipoDoc());
                row.createCell(3).setCellValue(candidato.getDoc());
                row.createCell(4).setCellValue(candidato.getFechaExpedicion());
                row.createCell(5).setCellValue(candidato.getFechaNacimiento());
                row.createCell(6).setCellValue(candidato.isEstado() ? "Activo" : "Inactivo");
                row.createCell(7).setCellValue(candidato.isBlackListed()? "BlackListed" : "No");
                row.createCell(8).setCellValue(candidato.getWave().getNombre());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
