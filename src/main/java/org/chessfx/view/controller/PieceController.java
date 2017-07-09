/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.view.controller;

import java.util.List;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import org.chessfx.core.model.Square;
import org.chessfx.core.service.BoardService;
import org.chessfx.view.ChessBoardDrawer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Scope("prototype")
@Component("PieceController")
public class PieceController implements EventHandler{

    private Square square;
    private ImageView image;
    private ChessBoardDrawer drawer;
    private static Square selectedSquare;
    private static Effect effect;
    
    @Autowired
    private BoardService service;
    
    public PieceController(){
        selectedSquare = null;
    }
    
    public PieceController(Square square, ImageView image, ChessBoardDrawer drawer){
        this.square = square;
        this.image = image;
        this.drawer = drawer;
    }
    
    @Override
    public void handle(Event t) {
        if (selectedSquare == null){
            selectedSquare = Square.getSquare(square);
            effect = image.getEffect();
            ColorAdjust color = new ColorAdjust();
            color.setBrightness(0.8);
            color.setContrast(0.5);
            image.setEffect(color);
            List<Square> allowedSquares = service.getAllowedMovements(square);
            drawer.showAllowedMoves(allowedSquares);
            return;
        }
        if(selectedSquare.equals(square)){
            selectedSquare = null;
            image.setEffect(effect);
            drawer.draw();
        }
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setDrawer(ChessBoardDrawer drawer) {
        this.drawer = drawer;
    }
}
