/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.HTTP;
import org.json.JSONObject;

/**
 *
 * @author toktarkhan_n
 */
@WebServlet("/generate")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class App extends HttpServlet {

    public String getGreeting() {
        return "index.jsp";
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Part jsonFilePart = req.getPart("jsonFileUploader");
//        JSONObject jsonObject = getJSONObject(jsonFilePart);
        Path jsonFilePath = uploadFile(jsonFilePart);
        Part excelFilePart = req.getPart("excelFileUpload");
        Part pdfFilePart = req.getPart("pdfFileUpload");
        Path excelFilePath = uploadFile(excelFilePart);
        Path pdfFilePath = uploadFile(pdfFilePart);

//        PdfGenerator generator = new PdfGenerator(excelFilePath, pdfFilePath, jsonObject);
        PdfGenerator generator = new PdfGenerator(excelFilePath, pdfFilePath, jsonFilePath);
//        generator.generate();

    }

    public Path uploadFile(Part filePart) throws IOException {
        String fileName = filePart.getSubmittedFileName();

        OutputStream outputFile = null;
        InputStream filecontent = null;
        String dirName = fileName.split("\\.")[0];
        Path rootLocation = Paths.get(dirName);

        if (!Files.exists(Paths.get(dirName))) {
            Files.createDirectories(Paths.get(dirName));
        }

        Path destinationFile = rootLocation.resolve(
                Paths.get(fileName))
                .normalize().toAbsolutePath();

        try {
            outputFile = new FileOutputStream(new File(destinationFile.toString()));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                outputFile.write(bytes, 0, read);
            }

            return destinationFile;
        } catch (FileNotFoundException fne) {
        } finally {
            if (outputFile != null) {
                outputFile.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
        }
        return destinationFile; // TO DO
    }

    public JSONObject getJSONObject(Part jsonPart) throws IOException {
        InputStream filecontent = jsonPart.getInputStream();
        StringBuilder sb = new StringBuilder();
        byte[] jsonContent = filecontent.readAllBytes();

        for (byte b : jsonContent) {
            sb.append((char) b);
        }

        return new JSONObject(sb.toString());
    }
}
