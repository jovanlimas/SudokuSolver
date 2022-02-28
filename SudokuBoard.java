/**
 * Jovan Limas
 *
 * This is a SudokuBoard class.
 * It reads data from a file and loads them into a 2D array as a sudoku board.
 *
 * Keywords: recursive backtracking, sets, maps, 2D arrays
 *
 */

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class SudokuBoard {

    // instance field
    private int[][] boardContent = new int[9][9];

    // constructor
    /**
     * This method constructs a SudokuBoard object given a specified target file name.
     *
     * pre  : specified fileName is a valid path/file name. Otherwise, a FileNotFoundException is thrown.
     * post : a SudokuBoard Object is constructed.
     * @param fileName - file name
     * @throws FileNotFoundException
     */
    public SudokuBoard(String fileName) throws FileNotFoundException{
        Scanner console = new Scanner((new File(fileName)));

        int row = 0;
        while (console.hasNextLine()) {
            String line = console.nextLine();

            for (int i = 0; i < line.length(); i++) {
                String item = line.substring(i, i + 1);
                int number;

                if (item.equals(".")) {
                    number = 0;
                } else if (item.equals("1") || item.equals("2") || item.equals("3") || item.equals("4") ||
                        item.equals("5") || item.equals("6") || item.equals("7") || item.equals("8") ||
                        item.equals("9")){
                    number = Integer.parseInt(item);
                } else {
                    number = -1;
                }

                boardContent[row][i] = number;
            }
            row++;
        }
    }

    /**
     * This method solves the sudoku board by recursive backtracking.
     *
     * pre  : none
     * post : returns true after solving the sudoku board.
     * @return true when sudoku board has been solved.
     */
    public boolean solve() {
        if (!isValid()) {
            return false;
        }
        if (isSolved()) {
            return true;
        }
        int row, column;

        for (int i = 0; i < boardContent.length; i++) {
            for (int j = 0; j < boardContent[i].length; j++) {

                // checks if grid is unfilled
                if (boardContent[i][j] == 0) {
                    row = i;
                    column = j;
                    for (int num = 1; num <= 9; num++) {

                        // checks if placing num into the grid is lawful
                        if (!duplicateInColumn(column) && !duplicateInRow(row)) {
                            boardContent[i][j] = num;

                            // recur
                            if (solve()) {
                                return true;
                            } else {
                                boardContent[i][j] = 0;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method checks if sudoku board is solved.
     *
     * pre  : none
     * post : returns true if boardContent is solved, false if otherwise
     * @return boolean indicating if board is solved.
     */
    public boolean isSolved() {
        HashMap<Integer, Integer> tracker = new HashMap<>();

        for (int i = 0; i < boardContent.length; i++) {
            for (int j = 0; j < boardContent[i].length; j++) {
                if (tracker.containsKey(boardContent[i][j])) {
                    tracker.put(boardContent[i][j], tracker.get(boardContent[i][j]) + 1);
                } else {
                    tracker.put(boardContent[i][j], 1);
                }
            }
        }

        for(Integer i : tracker.values()) {
            if (i != 9) {
                return false;
            }
        }

        return isValid();
    }

    /**
     * This method checks if sudoku board is valid.
     *
     * pre  : none
     * post : returns true if boardContent is valid, false if otherwise.
     * @return boolean indicating if sudoku board is valid.
     */
    public boolean isValid() {
        HashSet<Integer> data = new HashSet<>();
        data.add(1);
        data.add(2);
        data.add(3);
        data.add(4);
        data.add(5);
        data.add(6);
        data.add(7);
        data.add(8);
        data.add(9);
        data.add(0);

        // valid data check
        if (isNotValidData(data)) {
            return false;
        }

        // row check
        for (int i = 0; i < 9; i++) {
            if (duplicateInRow(i)) {
                return false;
            }
        }

        // column check
        for (int i = 0; i < 9; i++) {
            if (duplicateInColumn(i)) {
                return false;
            }
        }

        // miniSquare check
        for (int i = 1; i <= 9; i++) {
            int[][] miniSquare = miniSquare(i);
            if (duplicateInMiniSquare(miniSquare)) {
                return false;
            }
        }

        // returns true if passes all tests
        return true;
    }

    /**
     * This method checks if sudoku board data is valid.
     *
     * pre  : none
     * post : returns true if boardContent contains invalid data, false if otherwise
     * @param data - set of acceptable/valid integers
     * @return boolean indicating if data is not valid
     */
    private boolean isNotValidData(HashSet<Integer> data) {
        for (int i = 0; i < boardContent.length; i++) {
            for (int j = 0; j < boardContent[i].length; j++) {
                if (!data.contains(boardContent[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks if there are any duplicates in a specified row.
     *
     * pre  : rowNum must be in range
     * post : returns true if row contains duplicate, false if otherwise
     * @param rowNum - specified row number (0-8)
     * @return boolean indicating if there is a duplicate in the row.
     */
    private boolean duplicateInRow(int rowNum) {
        HashSet<Integer> row = new HashSet<>();

        for (int i = 0; i < 9; i++) {
            if (boardContent[rowNum][i] != 0 && !row.add(boardContent[rowNum][i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if there are any duplicates in a specified column.
     *
     * pre  : columnNum must be in range
     * post : returns true if column contains duplicate, false if otherwise
     * @param columnNum - specified column number (0-8)
     * @return boolean indicating if there is a duplicate in the column.
     */
    private boolean duplicateInColumn(int columnNum) {
        HashSet<Integer> column = new HashSet<>();

        for (int i = 0; i < 9; i++) {
            if (boardContent[i][columnNum] != 0 && !column.add(boardContent[i][columnNum])) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if there are any duplicates in a specified mini square.
     *
     * pre  : miniSquare Num must be a 3x3 array
     * post : returns true if miniSquare contains duplicate, false if otherwise
     * @param miniSquare - a 3x3 array
     * @return boolean indicating if there is a duplicate in the mini square.
     */
    private boolean duplicateInMiniSquare(int[][] miniSquare) {
        HashSet<Integer> miniSquareSet = new HashSet<>();

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                if (miniSquare[j][k] != 0 && !miniSquareSet.add(miniSquare[j][k])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method constructs and returns a 3x3 array in a specified spot.
     *
     * pre  : spot must be in range
     * post : content of the mini 3x3 sudoku board in the specified spot is returned as a 3x3 array
     * @param spot - specified spot
     * @return mini - a 3x3 array.
     */
    private int[][] miniSquare(int spot) {
        int[][] mini = new int[3][3];
        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 3; c++) {
                // This translates between the "spot" in the 9x9 Sudoku board
                // and a new mini square of 3x3
                mini[r][c] = boardContent[(spot - 1) / 3 * 3 + r][(spot - 1) % 3 * 3 + c];
            }
        }
        return mini;
    }

    // pre  : none
    // post : the content of the sudoku board is returned as a string
    @Override
    public String toString() {
        String printed = "";
        for (int i = 0; i < boardContent.length; i++) {

            for (int j = 0; j < boardContent[i].length; j++) {
                printed += boardContent[i][j] + "\t";
            }

            printed += "\n";
        }
        return printed;
    }
}
