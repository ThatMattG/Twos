package Twos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartScreen {

    private Scene scene;
    private Stage stage;
    private String title;

    public StartScreen(Stage stage) throws Exception {
        this.stage = stage;
        title = "Play Twos.";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
        StartScreenController controller = new StartScreenController(stage);
        loader.setController(controller);
        Parent root = loader.load();

        scene = new Scene(root);
    }

    public void show() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}