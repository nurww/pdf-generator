/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
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
    private JSONObject jsonObject;

    public PdfGenerator() {
    }

    public PdfGenerator(Path excelFilePath, Path pdfFilePath, JSONObject jsonObject) {
        this.excelFilePath = excelFilePath;
        this.pdfFilePath = pdfFilePath;
        this.jsonObject = jsonObject;
    }

    public void generate() throws IOException {
        try {
            System.out.println("in generator");
//        json();
            excel();
//        pdf();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setExcelPath(Path excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    public void setPdfPath(Path pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    private void excel() throws FileNotFoundException, IOException, DocumentException {

        FileInputStream file = new FileInputStream(new File(excelFilePath.toString()));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        Row firstRow = sheet.getRow(0);

        int numberOfRows = sheet.getPhysicalNumberOfRows();
        String cell;

        Map<String, List<InputConfigurator>> hashMap = new HashMap();

        for (int i = 1; i < numberOfRows; i++) {
            ArrayList<InputConfigurator> list = new ArrayList();
            for (int j = 0; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {
                cell = sheet.getRow(i).getCell(j).toString();

                InputConfigurator inputConfigurator = json(firstRow.getCell(j).toString());
                inputConfigurator.setInputValue(cell);

                list.add(inputConfigurator);

            }
            hashMap.put(i + "", list);

        }
        System.out.println(pdfFilePath);
//        pdf(hashMap);

    }

    private void pdf(Map<String, List<InputConfigurator>> hashMap) throws FileNotFoundException, IOException, DocumentException {

        int name = 0;
        for (Map.Entry<String, List<InputConfigurator>> entry : hashMap.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            List<InputConfigurator> list = entry.getValue();

            PdfReader pdfReader = new PdfReader(pdfFilePath.toString());
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(name + ".pdf"));
            System.out.println(name + ".pdf");
            name++;

//            BaseFont bf = BaseFont.createFont("c:/windows/fonts/times.ttf","Identity-H", BaseFont.EMBEDDED);
            
            BaseFont baseFont = BaseFont.createFont(
                    BaseFont.TIMES_ROMAN,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED
            );

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
    }

    private InputConfigurator json(String columnName) throws IOException {

        InputConfigurator inputConfigurator = new InputConfigurator();

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

        return inputConfigurator;
    }
}
