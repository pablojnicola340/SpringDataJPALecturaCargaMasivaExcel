package org.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    private static final int TOTAL_RECORDS = 1000000;
    private static final String RELATIVE_PATH = "../customers.xlsx";

    public static void main(String[] args) {
        //Implementacion con Apache poi
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("LastName");
        headerRow.createCell(3).setCellValue("Address");
        headerRow.createCell(4).setCellValue("Email");

        for (int i = 1; i <= TOTAL_RECORDS; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue("name" + i);
            row.createCell(2).setCellValue("lastName" + i);
            row.createCell(3).setCellValue("address" + i);
            row.createCell(4).setCellValue("email" + i);
        }

        // Imprime la ruta absoluta antes de guardar el archivo
        System.out.println("Guardando el archivo en: " + new File(RELATIVE_PATH).getAbsolutePath());

        try (FileOutputStream fileOut = new FileOutputStream(RELATIVE_PATH)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        }
    }
}