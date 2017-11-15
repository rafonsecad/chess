package org.chessfx.application;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.chessfx.core.configuration.AppConfig;
import org.chessfx.application.controller.BoardController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            ChessBoardDrawer boardDrawer = (ChessBoardDrawer) context.getBean("ChessBoardDrawer");
            BoardController boardController = (BoardController) context.getBean("BoardController");
            Pane pane = new Pane();
            VBox notation = buildNotationPane();
            boardDrawer.setNotationPane(notation);
            boardDrawer.setPane(pane);
            boardController.init(boardDrawer);
            pane.setOnMouseClicked(boardController);
            stage.setTitle("Chess FX");
            BorderPane borderPane = new BorderPane();
            HBox hbox = new HBox();
            Button buttonRestart = new Button("Restart");
            buttonRestart.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t){
                    boardController.init(boardDrawer);
                }
            });
            buttonRestart.setPrefSize(100, 20);
            hbox.getChildren().addAll(buttonRestart);
            
            borderPane.setTop(hbox);
            borderPane.setCenter(pane);
            borderPane.setRight(notation);
            stage.setScene(new Scene(borderPane));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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