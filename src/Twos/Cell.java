package Twos;


import javafx.beans.property.SimpleIntegerProperty;

public class Cell extends SimpleIntegerProperty {

    private boolean canCombine;

    public Cell(int initialValue) {
        super(initialValue);
        canCombine = true;
    }

    public void hasCombined() {
        if (super.get() == 0) {
            canCombine = true;
        } else {
            canCombine = false;
        }
    }

    public boolean canCombine() {
        return canCombine;
    }

    public void resetCombine() {
        canCombine = true;
    }

    public void setValue(int num) {
        super.setValue((Number) num);
    }
}