/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.view.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import org.chessfx.core.model.Square;

/**
 *
 * @author rafael
 */
public class PieceController implements EventHandler{

    private Square square;
    private ImageView image;
    private static Square selectedSquare;
    private static Effect effect;
    
    public PieceController(){
        selectedSquare = null;
    }
    
    public PieceController(Square square, ImageView image){
        this.square = square;
        this.image = image;
    }
    
    @Override
    public void handle(Event t) {
        System.out.print(square.getFile());
        System.out.println(square.getRank());
        System.out.println(square.getPiece().getTeam() + " " + square.getPiece().getType());
        if (selectedSquare == null){
            selectedSquare = Square.getSquare(square);
            effect = image.getEffect();
            ColorAdjust color = new ColorAdjust();
            color.setBrightness(0.8);
            color.setContrast(0.5);
            image.setEffect(color);
            return;
        }
        if(selectedSquare.equals(square)){
            selectedSquare = null;
            image.setEffect(effect);
        }
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
