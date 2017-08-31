/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.view;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.service.GraphicsService;
import org.chessfx.view.model.SquareImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component("ChessBoardDrawer")
public class ChessBoardDrawer {
    
    @Autowired
    private GraphicsService graphsService;
    
    private final int WIDTH = 80;
    private Pane pane;
    
    public void draw(List<SquareImage> squares, List<Piece> deadPieces){
        pane.getChildren().clear();
        drawDeadPiecesPanes(deadPieces);
        squares.stream().forEach(square -> {
            drawSquare(square);
        });
    }
    
    private void drawDeadPiecesPanes(List<Piece> deadPieces){
        drawLateralPane(0, 2*WIDTH, 8*WIDTH);
        drawLateralPane(10*WIDTH, 2*WIDTH, 8*WIDTH);
        
        List<Piece> whitePieces = deadPieces.stream()
                .filter(piece -> piece.getTeam() == Team.WHITE)
                .collect(Collectors.toList());
        
        arrangeDeadPieces(whitePieces, 0);
        
        List<Piece> blackPieces = deadPieces.stream()
                .filter(piece -> piece.getTeam() == Team.BLACK)
                .collect(Collectors.toList());
        
        arrangeDeadPieces(blackPieces, 10);

    }
    
    private void drawLateralPane(int x ,int width, int height){
        Rectangle rectangle = new Rectangle();
        Color color = Color.SADDLEBROWN;
        rectangle.setX(x);
        rectangle.setY(0);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setFill(color);
        rectangle.setStroke(Color.BLACK);
        pane.getChildren().add(rectangle);
    }
    
    private void arrangeDeadPieces(List<Piece> pieces, int column){
        for(int index=0; index<pieces.size(); index++){
            int X_index = index < 8 ? column : column+1;
            int Y_index = index < 8 ? index : index-8;
            BufferedImage im = graphsService.getImage(pieces.get(index));
            Image i = SwingFXUtils.toFXImage(im, null);
            final ImageView selectedImage = new ImageView();
            selectedImage.setImage(i);
            selectedImage.setX(X_index*WIDTH);
            selectedImage.setY(Y_index*WIDTH);
            pane.getChildren().add(selectedImage);
        }
    }
    
    private void drawSquare(SquareImage square){
        pane.getChildren().add(drawEmptySquare(square));
        if(square.isOption()){
            showAllowedMove(square);
        }
        if (!square.isOcuppied()){
            return;
        }
        BufferedImage im = graphsService.getImage(square.getPiece());
        Image i = SwingFXUtils.toFXImage(im, null);
        final ImageView selectedImage = new ImageView();
        selectedImage.setImage(i);
        selectedImage.setX(getFileCoordinate(square.getFile()));
        selectedImage.setY(getRankCoordinate(square.getRank()));
        if (square.isSelected()){
            setSelectedEffect(selectedImage);
        }
        pane.getChildren().add(selectedImage);
        if(square.isOption()){
            showAllowedMove(square);
        }
    }
    
    private Rectangle drawEmptySquare(SquareImage square){
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
    
    private void showAllowedMove(SquareImage square){
        Circle circle = new Circle();
        circle.setCenterX(getFileCoordinate(square.getFile()) + (WIDTH*0.5));
        circle.setCenterY(getRankCoordinate(square.getRank()) + (WIDTH*0.5));
        circle.setRadius(WIDTH*0.25);
        circle.setFill(Color.LIGHTBLUE);
        pane.getChildren().add(circle);
    }
    
    private void setSelectedEffect(ImageView image){
        ColorAdjust color = new ColorAdjust();
        color.setBrightness(0.8);
        color.setContrast(0.5);
        image.setEffect(color);
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
        return (factor + 2)*WIDTH;
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
    
    public void setPane(Pane pane){
        this.pane = pane;
    }
}
