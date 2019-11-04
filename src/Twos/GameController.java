package Twos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private TwosGame game;
    private final double SQUARE_TEXT_SIZE = 32.0;
    Stage stage;

    @FXML private HBox boardRegion;
    @FXML private Label scoreLabel;
    @FXML private Button exitButton;

    private GridPane grid;
    private List<Label> squares;

    public GameController(Stage stage) {
        this.stage = stage;
        game = new TwosGame();
    }

    public void initialize(Parent root) {
        // Add squares (JavaFX labels) and bind with cells
        createSquares();
        initializeSquares();

        // Set other listeners
        addGameOverListener();
        addScoreListener();
    }

    private void createSquares() {
        grid = new GridPane();
        boardRegion.getChildren().add(grid);
        boardRegion.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
        squares = new ArrayList<>();
    }

    private void initializeSquares() {
        for (int rowNum = 0; rowNum < game.getSize(); rowNum++) {
            for (int colNum = 0; colNum < game.getSize(); colNum++) {
                Label square = new Label();
                IntegerProperty cell = game.getCell(rowNum, colNum);

                initializeSquare(square, cell);
                square.textProperty().bind(cell.asString());
                bindBackgroundColor(square, cell);

                grid.add(square, colNum, rowNum);
                squares.add(square);
            }
        }
    }

    private void initializeSquare(Label square, IntegerProperty cell) {
        square.setMinSize(100, 100);
        square.setFont(new Font(SQUARE_TEXT_SIZE));
        square.setAlignment(Pos.CENTER);
        setLabelColors(square, cell);
    }

    private void addGameOverListener() {
        game.getGameOverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldVal, Boolean newVal) {
                if (newVal == true) {
                    handleGameOver();
                }
            }
        });
    }

    private void addScoreListener() {
        game.getScoreProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                if (!game.isOver()){
                    updateScoreLabel();
                }
            }
        });

        updateScoreLabel();
    }

    private void updateScoreLabel() {
        if (game.isOver()){
            displayEndMessage();
        } else {
            scoreLabel.setText("Score: " + game.getScore());
        }
    }

    private void handleGameOver() {
        displayEndMessage();
        exitButton.setDisable(false);
    }

    private void displayEndMessage() {
        scoreLabel.setText("Game over.  Score: " + game.getScore());
    }

    private void bindBackgroundColor(Label square, IntegerProperty cell) {
        cell.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                setLabelColors(square, cell);
            }
        });
    }

    private void setLabelColors(Label square, IntegerProperty cell) {
        Color textColor = getTextColor(cell.getValue().intValue());
        setTextColor(square, textColor);

        String backgroundColor = getBackgroundColorCode(cell.getValue().intValue());
        setBackgroundColor(square, backgroundColor);

        square.setBorder(new Border(new BorderStroke(Color.SANDYBROWN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3.0))));
    }

    private String getBackgroundColorCode(int value) {
        if (value == 0) {
            return "000000";
        }

        // Use log base 2 for colour calculations
        int log = log2(value);

        int red =  (log * 41) % 59 + Math.min(10, log) * 8;

        // Subtract red here to reduce chance of brown/grey
        int green = (log * 51) % 75 + 70 - red / 2;

        // Subtract diff between red and green; if red and green are close then blue is likely to be high
        int blue = (log * 40) % 153 + 30 - Math.abs(red - green) / 5;


        int combined = (red << 16) | (green << 8) | blue;

        String code = Integer.toHexString(combined);
        code = padLeft(code, "0", 6);

        return code;
    }

    // Ignores negative numbers
    private int log2(int value) {
        if (value <= 1) {
            return 0;
        }
        return 1 + log2(value / 2);
    }

    public String padLeft(String str, String ch, int length) {
        if (ch.length() != 1) {
            return null;
        }

        while (str.length() < length) {
            str = ch.concat(str);
        }

        return  str;
    }

    private Color getTextColor(int value) {
        if (value == 0) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    private void setTextColor(Label square, Color textColor) {
        square.setTextFill(textColor);
    }

    private void setBackgroundColor(Label square, String backgroundColor) {
        square.setStyle("-fx-background-color: #".concat(backgroundColor));
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                game.moveUp();
                break;
            case DOWN:
                game.moveDown();
                break;
            case LEFT:
                game.moveLeft();
                break;
            case RIGHT:
                game.moveRight();
                break;
            default:
                break;
        }
    }

    public void handleExit() throws Exception {
        StartScreen screen = new StartScreen(stage);
        screen.show();
    }

}
