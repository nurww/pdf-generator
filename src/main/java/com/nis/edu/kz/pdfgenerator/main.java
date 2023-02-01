/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author toktarkhan_n
 */
public class main {

    public static void main(String[] args) throws IOException {

        Path resultDir = Paths.get("source/resultDir");
        Path pdfFilePath = Paths.get("source/sample.pdf");
        Path excelFilePath = Paths.get("source/sample.xlsx");
        Path jsonFilePath = Paths.get("source/sample.json");

//        Path resultDir = Paths.get(args[0]);
//        Path pdfFilePath = Paths.get(args[1]);
//        Path excelFilePath = Paths.get(args[2]);
//        Path jsonFilePath = Paths.get(args[3]);
        if (!Files.exists(Files.createDirectories(resultDir))) {
            Files.createDirectories(resultDir);
        }

        PdfGenerator generator = new PdfGenerator(resultDir, excelFilePath, pdfFilePath, jsonFilePath);
        generator.generate();
    }
}
