package pallo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Pallo using FXML.
 */
public class Main extends Application {

    private Pallo pallo = new Pallo();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            scene.getStylesheets().add(Main.class.getResource("/view/styles.css").toExternalForm());
            stage.setTitle("Pallo");
            stage.setScene(scene);
            stage.setMinWidth(300);
            stage.setMinHeight(400);
            fxmlLoader.<MainWindow>getController().setPallo(pallo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
