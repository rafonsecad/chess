/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.chessfx.application.model.ChessBoard;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author rafael
 */
public class ClientApp {
    private static ChessBoardDrawer drawer;
    
    public static void getApp(ConfigurableApplicationContext springContext, BorderPane appPane){
        drawer = (ChessBoardDrawer) springContext.getBean("ChessBoardDrawer");
        Pane chessBoard = new Pane();
        VBox notation = buildNotationPane();
        HBox upperMenu = buildUpperMenu();
        drawer.setNotationPane(notation);
        drawer.setPane(chessBoard);
        appPane.setTop(upperMenu);
        appPane.setCenter(chessBoard);
        appPane.setRight(notation);
    }
    
    public static void setChessBoard(ChessBoard board){
        Platform.runLater(()->drawer.draw(board));
    }
    
    private static HBox buildUpperMenu(){
        HBox upperMenu = new HBox();
        return upperMenu;
    }
    
    private static VBox buildNotationPane(){
        VBox notation = new VBox();
        notation.setStyle("-fx-background-color: black");
        Text movements = new Text("Movements");
        movements.setFill(Color.WHITE);
        notation.getChildren().add(movements);
        return notation;
    }
}
