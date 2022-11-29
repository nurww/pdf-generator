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
//        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        Document document = new Document();

        try {
            // creation of the different writers

            PdfWriter.getInstance(document, new FileOutputStream("sample.pdf", true));

            // we add some meta information to the document
//            document.addAuthor("Bruno Lowagie");
//            document.addSubject("This is the result of a Test.");
            // we open the document for writing
            document.open();

            document.add(new Paragraph("Hello world"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
        document.close();

        try {
            PdfReader pdfReader = new PdfReader("sample.pdf");
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("test.pdf"));
            BaseFont baseFont = BaseFont.createFont(
                    BaseFont.TIMES_ROMAN,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED
            );

            int pages = pdfReader.getNumberOfPages();

            for (int i = 1; i <= pages; i++) {
                PdfContentByte pageContentByte = pdfStamper.getOverContent(i);
                pageContentByte.beginText();
                pageContentByte.setFontAndSize(baseFont, 14);
                pageContentByte.setTextMatrix(50, 740);
                pageContentByte.showText("hello World");
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
