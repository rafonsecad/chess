package org.chessfx.view;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.chessfx.core.configuration.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            ChessBoardDrawer boardDrawer = (ChessBoardDrawer) context.getBean("ChessBoardDrawer");
            Pane pane = new Pane();
            boardDrawer.init(pane);
            stage.setTitle("Chess Fx");
            Group root = new Group();
            root.getChildren().add(pane);
            stage.setScene(new Scene(root));
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

}
