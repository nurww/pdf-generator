/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator.exceptions;

import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 *
 * @author toktarkhan_n
 */
public class CellIsNullException extends RuntimeException {

    public CellIsNullException(String message) {
        super(message);
    }

    public CellIsNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
