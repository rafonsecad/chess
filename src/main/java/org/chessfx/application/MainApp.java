package org.chessfx.application;

import org.chessfx.application.networkapp.NetworkApp;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "org.chessfx")
public class MainApp extends Application {

    private static ConfigurableApplicationContext springContext;
    
    @Override
    public void start(Stage stage) throws Exception {
        try {
            springContext = SpringApplication.run(MainApp.class);
            stage.setTitle("Chess FX");
            BorderPane appPane = new BorderPane();
            getMainMenu(appPane);
            stage.setScene(new Scene(appPane));
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
    
    public static void getMainMenu (BorderPane appPane){
        Pane menu = new Pane();
        Button singleGame = new Button("Single Computer Game");
        Button otherComputers =  new Button("Find Other Computers");
        
        singleGame.setOnMouseClicked((MouseEvent t) -> {
            SingleComputerApp.getApp(springContext, appPane);
        });
        otherComputers.setOnMouseClicked((MouseEvent t)->{
            NetworkApp.getApp(springContext, appPane);
        });
        menu.setPrefSize(13*80, 9*80);
        singleGame.setPrefSize(200, 20);
        singleGame.setLayoutX(6*80);
        singleGame.setLayoutY(4*80);
        otherComputers.setPrefSize(200, 20);
        otherComputers.setLayoutX(6*80);
        otherComputers.setLayoutY(5*80);
        menu.getChildren().add(singleGame);
        menu.getChildren().add(otherComputers);
        appPane.setCenter(menu);
    }
    
    @Override
    public void stop(){
        springContext.close();
    }
}