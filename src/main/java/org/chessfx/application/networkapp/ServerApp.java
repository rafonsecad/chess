/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.networkapp;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.chessfx.application.ChessBoardDrawer;
import org.chessfx.application.controller.BoardController;
import org.chessfx.application.controller.strategy.ServerBoardStrategy;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author rafael
 */
public class ServerApp {
    private static ChessBoardDrawer drawer;
    private static BoardController boardController;
    
    public static void getApp(ConfigurableApplicationContext springContext, BorderPane appPane, String ip){
        drawer = (ChessBoardDrawer) springContext.getBean("ChessBoardDrawer");
        boardController = (BoardController) springContext.getBean("BoardController");
        boardController.setStrategy(new ServerBoardStrategy(ip));
        Pane chessBoard = new Pane();
        VBox notation = buildNotationPane();
        HBox upperMenu = buildUpperMenu();
        drawer.setNotationPane(notation);
        drawer.setPane(chessBoard);
        boardController.init(drawer);
        chessBoard.setOnMouseClicked(boardController);
        appPane.setTop(upperMenu);
        appPane.setCenter(chessBoard);
        appPane.setRight(notation);
    }
    
    private static HBox buildUpperMenu(){
        HBox upperMenu = new HBox();
        Button buttonRestart = new Button("Restart");
        buttonRestart.setOnMouseClicked((MouseEvent t) -> {
            boardController.init(drawer);
        });
        buttonRestart.setPrefSize(100, 20);
        upperMenu.getChildren().addAll(buttonRestart);
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
    
    public static void requestMove(double x, double y){
        Platform.runLater(()-> boardController.processEvent(x, y));
    }
}
