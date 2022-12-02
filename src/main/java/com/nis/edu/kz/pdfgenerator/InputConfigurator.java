/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

/**
 *
 * @author toktarkhan_n
 */
public class InputConfigurator {

    String fontFamily;
    String fontSize;
    String title;
    String positionX;
    String positionY;
    String inputValue;

    public InputConfigurator() {
    }

    public InputConfigurator(String fontFamily, String fontSize, String title, String positionX, String positionY) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.title = title;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }

    @Override
    public String toString() {
        return "InputConfigurator{" + "fontFamily=" + fontFamily + ", fontSize=" + fontSize + ", title=" + title + ", positionX=" + positionX + ", positionY=" + positionY + ", inputValue=" + inputValue + '}';
    }

}
