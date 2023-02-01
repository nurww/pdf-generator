/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nis.edu.kz.pdfgenerator;

import java.util.Objects;

/**
 * @author toktarkhan_n
 */
public class BarcodeConfigurator extends Configurator {

    String type;
    String title;
    float positionX;
    float positionY;
    int width;
    int height;
    String inputValue;

    public BarcodeConfigurator() {
    }

    public BarcodeConfigurator(String title, float positionX, float positionY, int width, int height) {
        this.title = title;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "BarcodeConfigurator{" +
               "type='" + type + '\'' +
               ", title='" + title + '\'' +
               ", positionX=" + positionX +
               ", positionY=" + positionY +
               ", width=" + width +
               ", height=" + height +
               ", inputValue='" + inputValue + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarcodeConfigurator that = (BarcodeConfigurator) o;
        return Float.compare(that.positionX, positionX) == 0 && Float.compare(that.positionY, positionY) == 0 && Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0 && Objects.equals(type, that.type) && Objects.equals(title, that.title) && Objects.equals(inputValue, that.inputValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, positionX, positionY, width, height, inputValue);
    }
}
