package Twos;

import javafx.stage.Stage;

public class StartScreenController {

    private Stage stage;

    public StartScreenController(Stage stage) {
        this.stage = stage;
    }

    public void handleNewGame() throws Exception {
        GameScreen gameScreen = new GameScreen(stage);
        gameScreen.show();
    }

    public void handleExit() {
        System.exit(0);
    }

}
