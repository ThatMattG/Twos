package Twos;


import javafx.scene.paint.Color;

public class ColorCalculator {

    static Color calculateColor(int num) {
        Color color = new Color(num * 4 % 256, (100 - 10 * num) % 256, (10 * num) % 256, 0);
        return color;
    }

}
