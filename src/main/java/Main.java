import java.io.IOException;

import fiona.command.Fiona;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Fiona fiona = new Fiona("./data/fiona.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            // inject the Duke instance
            fxmlLoader.<MainWindow>getController().setDuke(fiona);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
