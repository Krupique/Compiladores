/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.utils;

import javafx.scene.image.ImageView;

/**
 *
 * @author Henrique K. Secchi
 */
public class Erros {
    private ImageView img;
    private int x;
    private int y;

    public Erros(ImageView img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
        
        this.img.setFitWidth(7);
        this.img.setFitHeight(7);
        this.img.setLayoutX(x);
        this.img.setLayoutY(y);
    }
    
    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
}
