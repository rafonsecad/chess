/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx;

import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component("ChessBoardDrawer")
public class ChessBoardDrawer {
    
    @Autowired
    private BoardService service;
    
    private final int WIDTH = 80;
    
    public void init(Pane pane){
        service.initBoard();
        Board board = service.getBoard();
        List<Square> squares = board.getSquares();
        squares.stream().forEach(s -> { 
            pane.getChildren().add(getSquareDrawed(s));
        });
    }
    
    private Rectangle getSquareDrawed(Square square){
        Rectangle rectangle = new Rectangle();
        rectangle.setX(getFileCoordinate(square.getFile()));
        rectangle.setY(getRankCoordinate(square.getRank()));
        rectangle.setWidth(WIDTH);
        rectangle.setHeight(WIDTH);
        Color color = square.isDarkColor() ? Color.DIMGRAY : Color.WHITE;
        rectangle.setFill(color);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }
    
    private int getFileCoordinate (char file){
        int factor = 0;
        switch(file){
            case 'a' : factor = 0; break;
            case 'b' : factor = 1; break;
            case 'c' : factor = 2; break;
            case 'd' : factor = 3; break;
            case 'e' : factor = 4; break;
            case 'f' : factor = 5; break;
            case 'g' : factor = 6; break;
            case 'h' : factor = 7; break;
        }
        return factor*WIDTH;
    }
    
    private int getRankCoordinate (int rank){
        int factor = 0;
        switch(rank){
            case 8 : factor = 0; break;
            case 7 : factor = 1; break;
            case 6 : factor = 2; break;
            case 5 : factor = 3; break;
            case 4 : factor = 4; break;
            case 3 : factor = 5; break;
            case 2 : factor = 6; break;
            case 1 : factor = 7; break;
        }
        return factor*WIDTH;
    }
}
