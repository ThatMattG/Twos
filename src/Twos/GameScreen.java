package Twos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScreen {

    private Scene scene;
    private Stage stage;
    private String title;

    public GameScreen(Stage stage) throws Exception {
        this.stage = stage;
        title = "Play Twos.";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Twos.fxml"));
        GameController controller = new GameController(stage);
        loader.setController(controller);
        Parent root = loader.load();
        controller.initialize(root);

        scene = new Scene(root);
        scene.setOnKeyPressed(e -> { controller.handleKeyPress(e); });

    }

    public void show() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
