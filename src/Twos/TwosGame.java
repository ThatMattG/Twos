package Twos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwosGame {

    private BooleanProperty gameOver;
    private IntegerProperty score;
    private int BOARD_SIZE = 4;
    private Cell[][] board;
    private Random rng;

    public TwosGame() {
        gameOver = new SimpleBooleanProperty(false);
        score = new SimpleIntegerProperty(0);

//        Create board and set cells to 0
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        makeZeros();

//        Place a 2 randomly on the board
        rng = new Random();
        placeRandomTwo();

//        checkColors();     // Delete this line once colors are finalised
    }

    // For testing colors only
    private void checkColors() {
        board[0][0].setValue(2);
        board[0][1].setValue(4);
        board[0][2].setValue(8);
        board[0][3].setValue(16);
        board[1][0].setValue(32);
        board[1][1].setValue(64);
        board[1][2].setValue(128);
        board[1][3].setValue(256);
        board[2][0].setValue(512);
        board[2][1].setValue(1024);
        board[2][2].setValue(2048);
        board[2][3].setValue(4096);
    }

    private void makeZeros() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = new Cell(0);
            }
        }
    }

    private void placeRandomTwo() {
        List<Coord> zeroCoords = getZeroCells();
        if (zeroCoords.isEmpty()) {
            System.out.println("Zero Coords is empty in PlaceRandomTwo()");
            gameOver.set(true);
        } else {
            Coord selection = chooseRandomCoord(zeroCoords);
            setCell(selection, 2);
        }
    }

    public BooleanProperty getGameOverProperty() {
        return gameOver;
    }

    public boolean isOver() {
        return gameOver.get();
    }

    public IntegerProperty getScoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public int getSize() {
        return BOARD_SIZE;
    }

    public void setCell(Coord selection, int newValue) {
        getValue(selection).set(newValue);
    }

    public void setCell(Cell cell, int newValue) {
        cell.set(newValue);
    }

    public void setCell(int row, int col, int newValue) {
        Cell cell = getCell(row, col);
        setCell(cell, newValue);
    }

    public Cell getValue(Coord selection) {
        return board[selection.getRow()][selection.getCol()];
    }

    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    public int getCellValue(int row, int col) {
        return getCell(row, col).get();
    }

    private int getCellValue(Cell cell) {
        return cell.get();
    }

    private int getCellValue(Coord coord) {
        return getCellValue(coord.getRow(), coord.getCol());
    }

    private Coord chooseRandomCoord(List<Coord> coords) {
        int index = rng.nextInt(coords.size());
        Coord selected = coords.get(index);
        return selected;
    }

    private List<Coord> getZeroCells() {
        List<Coord> zeroCoords = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (getCellValue(row, col) == 0) {
                    zeroCoords.add(new Coord(row, col));
                }
            }
        }

        return zeroCoords;
    }

    public void moveUp() {
        boolean successfulMove = false;

        for (int row = 1; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (moveValueUp(row, col)) {
                    successfulMove = true;
                }
            }
        }

        if (successfulMove) {
            System.out.println("moved up");
            endMove();
        }
    }

    public boolean moveValueUp(int row, int col) {
        boolean moved = false;

//        Base case: top row
        if (row == 0) {
            return moved;
        }

        Cell cell = getCell(row, col);
        int value = getCellValue(cell);
        Cell cellAbove = getCell(row - 1, col);
        int cellAboveValue = getCellValue(cellAbove);

//        Case: value is zero (empty cell) -> ignore
        if (value == 0) {
            // nothing
        }

//        Case: same value as cell above -> combine
        else if (cellAboveValue == value
                && cell.canCombine()
                && cellAbove.canCombine()) {
            combineCells(cellAbove, cell);
            moved = true;
        }

//        Case: empty cell above -> move up and recurse
        else if (cellAboveValue == 0) {
            swapCells(cellAbove, cell);
            moveValueUp(row - 1, col);
            moved = true;
        }

        return moved;
    }

    public void moveDown() {
        boolean successfulMove = false;

        for (int row = BOARD_SIZE - 2; row >= 0; row--) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (moveValueDown(row, col)) {
                    successfulMove = true;
                }
            }
        }

        if (successfulMove) {
            System.out.println("moved down");
            endMove();
        }
    }

    public boolean moveValueDown(int row, int col) {
        boolean moved = false;

//        Base case: top row
        if (row == BOARD_SIZE - 1) {
            return moved;
        }

        Cell cell = getCell(row, col);
        int value = getCellValue(cell);
        Cell cellBelow = getCell(row + 1, col);
        int cellBelowValue = getCellValue(cellBelow);

//        Case: value is zero (empty cell) -> ignore
        if (value == 0) {
            // nothing
        }

//        Case: same value as cell below -> combine
        else if (cellBelowValue == value
                && cell.canCombine()
                && cellBelow.canCombine()) {
            combineCells(cellBelow, cell);
            moved = true;
        }

//        Case: empty cell above -> move up and recurse
        else if (cellBelowValue == 0) {
            swapCells(cellBelow, cell);
            moveValueDown(row + 1, col);
            moved = true;
        }

        return moved;
    }

    public void moveLeft() {
        boolean successfulMove = false;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 1; col < BOARD_SIZE; col++) {
                if (moveValueLeft(row, col)) {
                    successfulMove = true;
                }
            }
        }

        if (successfulMove) {
            System.out.println("moved left");
            endMove();
        }
    }

    public boolean moveValueLeft(int row, int col) {
        boolean moved = false;

//        Base case: top row
        if (col == 0) {
            return moved;
        }

        Cell cell = getCell(row, col);
        int value = getCellValue(cell);
        Cell cellLeft = getCell(row, col - 1);
        int cellLeftValue = getCellValue(cellLeft);

//        Case: value is zero (empty cell) -> ignore
        if (value == 0) {
            // nothing
        }

//        Case: same value as cell left -> combine
        else if (cellLeftValue == value
                && cell.canCombine()
                && cellLeft.canCombine()) {
            combineCells(cellLeft, cell);
            moved = true;
        }

//        Case: empty cell left -> move left and recurse
        else if (cellLeftValue == 0) {
            swapCells(cellLeft, cell);
            moveValueLeft(row, col - 1);
            moved = true;
        }

        return moved;
    }

    public void moveRight() {
        boolean successfulMove = false;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = BOARD_SIZE - 2; col >= 0; col--) {
                if (moveValueRight(row, col)) {
                    successfulMove = true;
                }
            }
        }

        if (successfulMove) {
            System.out.println("moved right");
            endMove();
        }
    }

    public boolean moveValueRight(int row, int col) {
        boolean moved = false;

//        Base case: top row
        if (col == BOARD_SIZE - 1) {
            return moved;
        }

        Cell cell = getCell(row, col);
        int value = getCellValue(cell);
        Cell cellRight = getCell(row, col + 1);
        int cellRightValue = getCellValue(cellRight);

//        Case: value is zero (empty cell) -> ignore
        if (value == 0) {
            // nothing
        }

//        Case: same value as cell right -> combine
        else if (cellRightValue == value
                && cell.canCombine()
                && cellRight.canCombine()) {
            combineCells(cellRight, cell);
            moved = true;
        }

//        Case: empty cell right -> move right and recurse
        else if (cellRightValue == 0) {
            swapCells(cellRight, cell);
            moveValueRight(row, col + 1);
            moved = true;
        }

        return moved;
    }

    private void combineCells(Cell cellToKeep, Cell cellToRemove) {
        int removeValue = getCellValue(cellToRemove);
        int keepValue = getCellValue(cellToKeep);

        setCell(cellToKeep, keepValue + removeValue);
        setCell(cellToRemove, 0);

        cellToKeep.hasCombined();
        cellToRemove.hasCombined();

    }

    private void swapCells(Cell cell1, Cell cell2) {
        int cellValue1 = cell1.getValue();
        int cellValue2 = cell2.getValue();

        setCell(cell1, cellValue2);
        setCell(cell2, cellValue1);
    }

    private void endMove() {
        placeRandomTwo();
        checkIfGameOver();

        int newScore = score.get() + 1;
        score.setValue(Integer.valueOf(newScore));

        System.out.println(score.get());
        resetCombines();

        System.out.println("\n\n\n");
        printBoard();   // Delete this line
    }

    private void checkIfGameOver() {
        List<Coord> zeroCoords = getZeroCells();
        if (zeroCoords.isEmpty()
                && numAdjacentMatchingCells() == 0) {
            System.out.println("Game over");
            gameOver.set(true);
        }
    }

    private void resetCombines() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = getCell(row, col);
                cell.resetCombine();
            }
        }
    }

    private int numAdjacentMatchingCells() {
        int count = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int cellVal = getCellValue(row, col);

                if (row < BOARD_SIZE - 1
                        && getCellValue(row + 1, col) == cellVal) {
                    count++;
                }

                if (col < BOARD_SIZE - 1
                        && getCellValue(row, col + 1) == cellVal) {
                    count++;
                }
            }
        }
        return count;
    }

    private void printBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = getCell(row, col);
                String valStr = cell.getValue().toString();
                System.out.printf(padLeft(valStr, " ", 6));
            }
            System.out.println("");
        }
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

}