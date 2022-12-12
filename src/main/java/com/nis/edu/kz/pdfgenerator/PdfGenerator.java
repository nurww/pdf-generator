/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.nis.edu.kz.pdfgenerator.exceptions.CellIsNullException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author toktarkhan_n
 */
public class PdfGenerator {

    private Path excelFilePath;
    private Path pdfFilePath;
    private Path jsonFilePath;
    private Path dirName;
    Path pdfFilesDirectory;

    public PdfGenerator() {
    }

    public PdfGenerator(Path dirName, Path excelFilePath, Path pdfFilePath, Path jsonFilePath) {
        System.out.println("WE ARE AT CONSTRUCTOR");
        this.excelFilePath = excelFilePath;
        this.pdfFilePath = pdfFilePath;
        this.jsonFilePath = jsonFilePath;
        this.dirName = dirName;
    }

    public PdfGenerator(Path excelFilePath, Path pdfFilePath, Path jsonFilePath) {
        this.excelFilePath = excelFilePath;
        this.pdfFilePath = pdfFilePath;
        this.jsonFilePath = jsonFilePath;
    }

//    public PdfGenerator(Path excelFilePath, Path pdfFilePath, JSONObject jsonObject) {
//        this.excelFilePath = excelFilePath;
//        this.pdfFilePath = pdfFilePath;
//        this.jsonFilePath = jsonFilePath;
//    }
    public void generate() throws IOException {
        System.out.println("in generator");
        excel();

    }

    public void setExcelPath(Path excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    public void setPdfPath(Path pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public void setJsonFilePath(Path jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    private void excel() {

        FileInputStream file = null;
        try {
//           ENCODING charset
            file = new FileInputStream(new File(excelFilePath.toString()));
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFCell cell;
            Row firstRow = sheet.getRow(0);
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            Map<String, List<InputConfigurator>> hashMap = new HashMap();

            String fileName = sheet.getRow(0).getCell(0).toString();

            for (int i = 1; i < numberOfRows; i++) {
                XSSFRow row = sheet.getRow(i);
                if (isEmptyRow(row)) {
                    continue;
                }
                ArrayList<InputConfigurator> list = new ArrayList();
                for (int j = 1; j < row.getPhysicalNumberOfCells(); j++) {
                    cell = sheet.getRow(i).getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    if (cell.toString().equals("")) {
                        String message = "cell " + cell.getAddress() + " is empty";
                        throw new CellIsNullException(message);
                    }
                    InputConfigurator inputConfigurator = json(firstRow.getCell(j).toString());
                    inputConfigurator.setInputValue(cell.toString());
                    list.add(inputConfigurator);
                }
                hashMap.put(fileName + "_" + i, list);

            }
            pdf(hashMap);
            zipFiles();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean isEmptyRow(XSSFRow row) {
        boolean isEmpty = false;

        if (row == null) {
            isEmpty = true;
            return isEmpty;
        }
        for (int j = 1; j < row.getPhysicalNumberOfCells(); j++) {
            XSSFCell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            isEmpty = cell.toString().equals("");
            if (isEmpty == true) {
                return isEmpty;
            }

        }
        return isEmpty;
    }

    private void pdf(Map<String, List<InputConfigurator>> hashMap) {
        try {

            pdfFilesDirectory = Paths.get(pdfFilePath.getParent() + "/pdf_results_directory");
            if (!Files.exists(pdfFilesDirectory)) {
                Files.createDirectories(pdfFilesDirectory);
            }

            for (Map.Entry<String, List<InputConfigurator>> entry : hashMap.entrySet()) {

                List<InputConfigurator> list = entry.getValue();
                String name = entry.getKey();

                PdfReader pdfReader = new PdfReader(pdfFilePath.toString());
                PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(pdfFilesDirectory + "/" + name + ".pdf"));
                System.out.println(name + ".pdf");

                BaseFont baseFont = BaseFont.createFont("/timesnewromanpsmt.ttf", "Identity-H", BaseFont.EMBEDDED);

                try {

                    int pages = pdfReader.getNumberOfPages();

                    for (int i = 1; i <= pages; i++) {
                        PdfContentByte pageContentByte = pdfStamper.getOverContent(i);
                        pageContentByte.beginText();

                        for (InputConfigurator configurator : list) {
                            float cordX = (float) (Float.parseFloat(configurator.getPositionX()) / 1.2);
                            float cordY = (float) (Float.parseFloat(configurator.getPositionY()) / 1.2);
                            String fontFamily = configurator.getFontFamily();
                            String fontSize = configurator.getFontSize();
                            String title = configurator.getTitle();
                            float height;
                            float width;
                            float calculatedCordX;
                            float calculatedCordY;
                            String textReplace = configurator.getInputValue();

                            pageContentByte.setFontAndSize(baseFont, 14);

                            height = pdfReader.getPageSize(i).getHeight();
                            width = pdfReader.getPageSize(i).getWidth();

                            calculatedCordX = cordX;
                            calculatedCordY = height - cordY;

                            pageContentByte.setTextMatrix(calculatedCordX, calculatedCordY);
                            pageContentByte.showText(textReplace);
                        }

                        pageContentByte.endText();
                    }
                    pdfStamper.close();
                    System.out.println("Pdf modified Succedfully");

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);

        } catch (DocumentException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private InputConfigurator json(String columnName) throws IOException {

//        InputStream filecontent = jsonPart.getInputStream();
        InputStream fileContent = new FileInputStream(new File(jsonFilePath.toString()));
        StringBuilder sb = new StringBuilder();
        byte[] jsonContent = fileContent.readAllBytes();

        for (byte b : jsonContent) {
            sb.append((char) b);
        }

        InputConfigurator inputConfigurator = new InputConfigurator();
        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONArray jsonNames = jsonObject.names();
        JSONArray jsonArray = jsonObject.toJSONArray(jsonNames);

        for (Iterator<Object> it = jsonArray.iterator(); it.hasNext();) {
            JSONObject obj = (JSONObject) it.next();
            if (columnName.equals(obj.getString("title"))) {
                String fontFamily = obj.getString("fontFamily");
                String fontSize = obj.getString("fontSize");
                String title = obj.getString("title");
                int positionX = obj.getJSONObject("body").getInt("x");
                int positionY = obj.getJSONObject("body").getInt("y");

                inputConfigurator.setFontFamily(fontFamily);
                inputConfigurator.setFontSize(fontSize);
                inputConfigurator.setTitle(title);
                inputConfigurator.setPositionX(String.valueOf(positionX));
                inputConfigurator.setPositionY(String.valueOf(positionY));
            }
        }

        if (inputConfigurator.getTitle() != null) {
            return inputConfigurator;
        }

        return null;
    }

    public void zipFiles() throws IOException {

        List<File> pdfFileNames = new ArrayList<>();

        Stream<Path> pdfFiles = Files.walk(pdfFilesDirectory);
        pdfFiles.forEach(f -> {
            if (!Files.isDirectory(f)) {
                pdfFileNames.add(f.toFile());
            }
        });

        final FileOutputStream fos = new FileOutputStream(dirName + "/compressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (File srcFile : pdfFileNames) {
            FileInputStream fis = new FileInputStream(srcFile);
            ZipEntry zipEntry = new ZipEntry(srcFile.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
    }
}
