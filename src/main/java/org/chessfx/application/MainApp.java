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
import org.springframework.core.io.ClassPathResource;

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
            Scene scene = new Scene(appPane);
            ClassPathResource resource = new ClassPathResource("/styles/Styles.css");
            String path = resource.getPath();
            scene.getStylesheets().add(path);
            stage.setScene(scene);
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
        singleGame.setLayoutX(6*80);
        singleGame.setLayoutY(4*80);
        singleGame.getStyleClass().add("ipad-grey");
        otherComputers.setLayoutX(6*80);
        otherComputers.setLayoutY(5*80);
        otherComputers.getStyleClass().add("ipad-grey");
        menu.getChildren().add(singleGame);
        menu.getChildren().add(otherComputers);
        appPane.setCenter(menu);
        appPane.getStyleClass().add("main-menu");
    }
    
    @Override
    public void stop(){
        springContext.close();
    }
}