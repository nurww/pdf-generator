/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author toktarkhan_n
 */
public class PdfGenerator {

    public void generate() throws IOException {

        excel();
        pdf();
        json();
    }

    public void excel() throws FileNotFoundException, IOException {
        FileInputStream file = new FileInputStream(new File("sample.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        Row firstRow = sheet.getRow(0);

        String cell;

        for (int i = 0; i < firstRow.getPhysicalNumberOfCells(); i++) {
            cell = firstRow.getCell(i).toString();
            System.out.println(cell);
        }
    }

    public void pdf() throws FileNotFoundException {
        
        float cordX = 227;
        float cordY = 200;
        float height;
        float width;
        float calculatedCordX;
        float calculatedCordY;
        String textReplace= "text to replace";

        try {
            PdfReader pdfReader = new PdfReader("sample.pdf");
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("result.pdf"));
            BaseFont baseFont = BaseFont.createFont(
                    BaseFont.TIMES_ROMAN,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED
            );

            int pages = pdfReader.getNumberOfPages();

            for (int i = 1; i <= pages; i++) {
                PdfContentByte pageContentByte = pdfStamper.getOverContent(i);
                pageContentByte.beginText();
                pageContentByte.setFontAndSize(baseFont, 14);

                
                height = pdfReader.getPageSize(i).getHeight();
                width = pdfReader.getPageSize(i).getWidth();
                
                calculatedCordX = width - cordX;
                calculatedCordY = height - cordY;

                pageContentByte.setTextMatrix(calculatedCordX, calculatedCordY);
                pageContentByte.showText(textReplace);
                pageContentByte.endText();
            }
            pdfStamper.close();
            System.out.println("Pdf modified Succedfully");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void json() {

    }
}
