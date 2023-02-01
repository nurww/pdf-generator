/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

/**
 *
 * @author toktarkhan_n
 */
public class InputConfigurator extends Configurator {

    String fontFamily;
    float fontSize;
    String title;
    float positionX;
    float positionY;
    String inputValue;

    public InputConfigurator() {
    }

    public InputConfigurator(String fontFamily, float fontSize, String title, float positionX, float positionY) {
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

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    @Override
    public String toString() {
        return "InputConfigurator{" + "fontFamily=" + fontFamily + ", fontSize=" + fontSize + ", title=" + title + ", positionX=" + positionX + ", positionY=" + positionY + ", inputValue=" + inputValue + '}';
    }

}
