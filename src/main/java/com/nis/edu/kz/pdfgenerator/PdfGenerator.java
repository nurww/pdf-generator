/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;

/**
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

    public void generate() throws IOException {
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
            file = new FileInputStream(excelFilePath.toString());
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row firstRow = sheet.getRow(0);
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            Map<String, List<Configurator>> hashMap = new HashMap<>();

            for (int i = 1; i < numberOfRows; i++) {
                XSSFRow row = sheet.getRow(i);
                Iterator<Cell> cellIterator = row.cellIterator();
                ArrayList<Configurator> list = new ArrayList<>();

                while (cellIterator.hasNext()) {
                    Cell c = cellIterator.next();
                    int columnIndex = c.getColumnIndex();

                    Configurator inputConfigurator = null;
                    if (firstRow.getCell(columnIndex) != null) {
                        inputConfigurator = json(firstRow.getCell(columnIndex).toString());
                    }

                    if (inputConfigurator != null) {
                        String cellValue = getTextFrom(i, columnIndex, sheet);
                        inputConfigurator.setInputValue(cellValue);
                        list.add(inputConfigurator);
                    }
                }

                String numString = getFileName(i);
                hashMap.put(numString, list);

            }
            pdf(hashMap);
            zipFiles();
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

    public String getFileName(int i) {
        StringBuilder numString = new StringBuilder("00000");
        int numStart = numString.length() - String.valueOf(i).length();
        numString.replace(numStart, numString.length(), String.valueOf(i));

        return String.valueOf(numString);
    }

    private static String getTextFrom(int r, int c, XSSFSheet sheet) {
        Row row = sheet.getRow(r);
        Cell cell = row.getCell(c);

        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        DataFormatter dataFormatter = new DataFormatter();

        if (cellType.equals(NUMERIC)) {
            return dataFormatter.formatCellValue(cell);
        }
        return cell.getStringCellValue();
    }

//    public BufferedImage generateBarCode(String text, int width, int height) {
//
////        String text = "*" + code + page + "*";
////        BitMatrix matrix = getBitMatrix(config, code, page);
////        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        ImageIO.write(image, "png", baos);
////        Image iTextImage = Image.getInstance(baos.toByteArray());
////        iTextImage.setAbsolutePosition(235.0F, 15.0F);
////        iTextImage.scaleAbsoluteWidth(200.0F);
////        iTextImage.scaleAbsoluteHeight(40.0F);
////        content.addImage(iTextImage);
////        content.beginText();
////        content.setFontAndSize(bf, 8.0F);
////        content.showTextAligned(0, text, 310.0F, 5.0F, 0.0F);
////        content.endText();
//
//
//
//
//        Code128Writer barcodeWriter = new Code128Writer();
//        BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.CODE_128, width, height);
//        try {
////            MatrixToImageWriter.writeToPath(bitMatrix, "png", Paths.get("barcode.png"));
//
//            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", baos);
//            Image iTextImage = Image.getInstance(baos.toByteArray());
//            iTextImage.setAbsolutePosition(235.0F, 15.0F);
//            iTextImage.scaleAbsoluteWidth(200.0F);
//            iTextImage.scaleAbsoluteHeight(40.0F);
//            content.addImage(iTextImage);
//            content.beginText();
//            content.setFontAndSize(bf, 8.0F);
//            content.showTextAligned(0, text, 310.0F, 5.0F, 0.0F);
//            content.endText();
//
//            System.out.println("Barcode created!");
//        } catch (Exception ignored) {
//
//        }
//        return MatrixToImageWriter.toBufferedImage(bitMatrix);
//    }

    private void pdf(Map<String, List<Configurator>> hashMap) {
        try {

            pdfFilesDirectory = Paths.get(pdfFilePath.getParent() + "/pdf_results_directory");
            if (!Files.exists(pdfFilesDirectory)) {
                Files.createDirectories(pdfFilesDirectory);
            }

            for (Map.Entry<String, List<Configurator>> entry : hashMap.entrySet()) {

                List<Configurator> list = entry.getValue();
                String name = entry.getKey();

                PdfReader pdfReader = new PdfReader(pdfFilePath.toString());

                PdfStamper pdfStamper = new PdfStamper(pdfReader, Files.newOutputStream(Paths.get(pdfFilesDirectory + "/" + name + ".pdf")));

                try {
                    int pages = pdfReader.getNumberOfPages();

                    for (int i = 1; i <= pages; i++) {
                        PdfContentByte pageContentByte = pdfStamper.getOverContent(i);
                        pageContentByte.beginText();

                        for (Configurator configurator : list) {


                            if (configurator.getClass() == InputConfigurator.class) {

                                String fontFamily = ((InputConfigurator) configurator).getFontFamily();
                                float fontSize = ((InputConfigurator) configurator).getFontSize();
                                BaseFont baseFont = getFont(fontFamily);
                                pageContentByte.setFontAndSize(baseFont, fontSize);

                                float calculatedCordX = configurator.getPositionX();
                                float calculatedCordY = configurator.getPositionY();
                                float height = pdfReader.getPageSize(i).getHeight();

                                calculatedCordY = height - calculatedCordY;
                                String textReplace = configurator.getInputValue();
                                pageContentByte.setTextMatrix(calculatedCordX, calculatedCordY);
                                pageContentByte.showText(textReplace);
                            } else if (configurator.getClass() == BarcodeConfigurator.class) {

//                                Image img = Image.getInstance("source/barcode.png");
                                String text = configurator.getInputValue();
                                int width = ((BarcodeConfigurator) configurator).getWidth();
                                int height = ((BarcodeConfigurator) configurator).getHeight();
                                float x = configurator.getPositionX();
                                float y = configurator.getPositionY();
                                y = pdfReader.getPageSize(i).getHeight() - y;

//                                generateBarCode(text, width, height);


                                Code128Writer barcodeWriter = new Code128Writer();
                                BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.CODE_128, width, height);
                                try {
//            MatrixToImageWriter.writeToPath(bitMatrix, "png", Paths.get("barcode.png"));


                                    BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ImageIO.write(image, "png", baos);
                                    Image iTextImage = Image.getInstance(baos.toByteArray());
                                    iTextImage.setAbsolutePosition(x, y);



                                    pageContentByte.addImage(iTextImage);
                                    BaseFont baseFont = BaseFont.createFont("/verdana.ttf", "Identity-H", BaseFont.EMBEDDED);
                                    pageContentByte.setFontAndSize(baseFont, 8.0F);
//                                    pageContentByte.showTextAligned(0, text, x + (width * 0.4F), y - (height * 0.3F), 0.0F);
                                    pageContentByte.showTextAligned(0, text, x + (width * 0.4F), y - 10, 0.0F);


                                    System.out.println("Barcode created!");
                                } catch (Exception ignored) {

                                }


//                                img.setAbsolutePosition(x, y);
//                                pageContentByte.addImage(img);
                            }
                        }

                        pageContentByte.endText();
                    }
                    pdfStamper.close();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            System.out.println("Pdf generated Successfully");
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private BaseFont getFont(String fontFamily) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont("/timesnewromanpsmt.ttf", "Identity-H", BaseFont.EMBEDDED);
        switch (fontFamily) {
            case ("Arial"):
                baseFont = BaseFont.createFont("/arial.ttf", "Identity-H", BaseFont.EMBEDDED);
                break;
            case ("Verdana"):
                baseFont = BaseFont.createFont("/verdana.ttf", "Identity-H", BaseFont.EMBEDDED);
                break;
            case ("Tahoma"):
                baseFont = BaseFont.createFont("/tahoma.ttf", "Identity-H", BaseFont.EMBEDDED);
                break;
            case ("Georgia"):
                baseFont = BaseFont.createFont("/georgia.ttf", "Identity-H", BaseFont.EMBEDDED);
                break;
            case ("Trebuchet MS"):
                baseFont = BaseFont.createFont("/trebuc.ttf", "Identity-H", BaseFont.EMBEDDED);
                break;
            default:
                baseFont = BaseFont.createFont("/timesnewromanpsmt.ttf", "Identity-H", BaseFont.EMBEDDED);
                break;
        }
        return baseFont;
    }

    private Configurator json(String columnName) throws IOException {

        InputStream fileContent = Files.newInputStream(new File(jsonFilePath.toString()).toPath());
        byte[] jsonContent = fileContent.readAllBytes();
        String s = new String(jsonContent, StandardCharsets.UTF_8);

        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonNames = jsonObject.names();
        JSONArray jsonArray = jsonObject.toJSONArray(jsonNames);

//        handleAsText();

        String title;
        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            title = obj.getString("title");
            String inputType = obj.getString("inputType");

            if (columnName.equals(title)) {

                if (inputType.equals("text")) {
                    InputConfigurator configurator = new InputConfigurator();

                    String fontFamily = obj.getString("fontFamily");
                    float fontSize = obj.getFloat("fontSize");
                    float positionX = obj.getJSONObject("body").getFloat("x");
                    float positionY = obj.getJSONObject("body").getFloat("y");

                    configurator.setFontFamily(fontFamily);
                    configurator.setFontSize(fontSize);
                    configurator.setTitle(title);
                    configurator.setPositionX(positionX);
                    configurator.setPositionY(positionY);
                    return configurator;
                } else if (inputType.equals("barcode")) {
                    BarcodeConfigurator configurator = new BarcodeConfigurator();

                    int width = obj.getInt("width");
                    int height = obj.getInt("height");
                    float positionX = obj.getJSONObject("body").getFloat("x");
                    float positionY = obj.getJSONObject("body").getFloat("y");
                    configurator.setWidth(width);
                    configurator.setHeight(height);
                    configurator.setTitle(title);
                    configurator.setPositionX(positionX);
                    configurator.setPositionY(positionY);

                    return configurator;
                }
            }
        }

        return null;
    }

//    private inputConfigurator

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
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
        System.out.println(".zip archival created Successfully");
    }
}
