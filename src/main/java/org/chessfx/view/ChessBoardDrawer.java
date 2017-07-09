/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.view;

import java.awt.image.BufferedImage;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.chessfx.core.configuration.AppConfig;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.service.BoardService;
import org.chessfx.core.service.GraphicsService;
import org.chessfx.view.controller.PieceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component("ChessBoardDrawer")
public class ChessBoardDrawer {
    
    @Autowired
    private BoardService boardService;
    
    @Autowired
    private GraphicsService graphsService;
    
    private AbstractApplicationContext context;
    private final int WIDTH = 80;
    private Pane pane;
    
    public void init(Pane pane){
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.pane = pane;
        boardService.initBoard();
        Board board = boardService.getBoard();
        List<Square> squares = board.getSquares();
        squares.stream().forEach(square -> {
            drawSquare(square, pane);
        });
    }
    
    public void draw(){
        pane.getChildren().clear();
        Board board = boardService.getBoard();
        List<Square> squares = board.getSquares();
        squares.stream().forEach(square -> {
            drawSquare(square, pane);
        });
    }
    
    public void showAllowedMoves(List<Square> movements){
        movements.stream().forEach(s -> {
            Circle circle = new Circle();
            circle.setCenterX(getFileCoordinate(s.getFile()) + (WIDTH*0.5));
            circle.setCenterY(getRankCoordinate(s.getRank()) + (WIDTH*0.5));
            circle.setRadius(WIDTH*0.25);
            circle.setFill(Color.LIGHTBLUE);
            pane.getChildren().add(circle);
        });
    }
    
    private void drawSquare(Square square, Pane pane){
        pane.getChildren().add(drawEmptySquare(square));
        if (!square.isOcuppied()){
            return;
        }
        BufferedImage im = graphsService.getImage(square.getPiece());
        Image i = SwingFXUtils.toFXImage(im, null);
        final ImageView selectedImage = new ImageView();
        selectedImage.setImage(i);
        selectedImage.setX(getFileCoordinate(square.getFile()));
        selectedImage.setY(getRankCoordinate(square.getRank()));
//        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        PieceController controller = (PieceController) context.getBean("PieceController");
        controller.setSquare(square);
        controller.setDrawer(this);
        controller.setImage(selectedImage);
        selectedImage.setOnMouseReleased(controller);
        pane.getChildren().add(selectedImage);
    }
    
    private Rectangle drawEmptySquare(Square square){
        Rectangle rectangle = new Rectangle ();
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
