package org.chessfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
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

            final ImageView selectedImage = new ImageView();

            boardDrawer.init(pane);
            InputStream in = getClass().getResourceAsStream("/pieces.png");
            BufferedImage bimage = ImageIO.read(in);
            BufferedImage rook = bimage.getSubimage(0, 0, 80, 80);
            BufferedImage rookScale = new BufferedImage(50, 50, rook.getType());
            Graphics2D g = rookScale.createGraphics();
            g.scale(0.6, 0.6);
            g.drawImage(rook, 0, 0, null);
            g.dispose();
            Image im = SwingFXUtils.toFXImage(rookScale, null);

            selectedImage.setImage(im);
            selectedImage.setX(0);
            selectedImage.setY(350);
            pane.getChildren().add(selectedImage);
            stage.setTitle("Drawing Operations Test");
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
