/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231159 - Mohammad Ferdinand Valliandra
 * 2 - 5026231135 - Fachreza Aptadhi Kurniawan
 * 3 - 5026231149 - Ananda Donelly Reksana
 */

package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int GRID_SIZE = 9; // Ukuran grid Sudoku
    private Cell[][] cells = new Cell[GRID_SIZE][GRID_SIZE];
    private Puzzle puzzle = new Puzzle();
    private SudokuMain sudokuMain; // Menyimpan referensi SudokuMain

    public GameBoardPanel(SudokuMain sudokuMain) {
        this.sudokuMain = sudokuMain; // Menyimpan referensi SudokuMain

        super.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        CellInputListener listener = new CellInputListener();
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }
        super.setPreferredSize(new Dimension(540, 540));
    }

    // Metode New Game dan Fitur Difficult
    public void newGame() {
        String[] options = {"Easy", "Medium", "Hard"};
        String selected = (String) JOptionPane.showInputDialog(
                this, "Select Difficulty", "New Game", JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);

        int difficulty;
        switch (selected) {
            case "Easy": difficulty = 40; break;
            case "Medium": difficulty = 30; break;
            case "Hard": difficulty = 20; break;
            default: difficulty = 30;
        }
        puzzle.newPuzzle(difficulty); // Membuat puzzle baru dengan tingkat Difficult yang dipilih

        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                // Hanya isi sel yang merupakan "given" (sel yang memiliki nilai tetap)
                if (puzzle.isGiven[row][col]) {
                    cells[row][col].newGame(puzzle.numbers[row][col], true);
                } else {
                    cells[row][col].newGame(0, false); // Biarkan kosong
                }
            }
        }

        sudokuMain.updateStatus();  // Perbarui status bar setelah game baru dimulai
    }

    // Metode Reset Game
    public void resetGame() {
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                // Hanya reset sel yang editable (bukan clue/given)
                if (cells[row][col].isEditable()) {
                    cells[row][col].reset(); // Kosongkan isi sel pemain
                }
            }
        }
        sudokuMain.updateStatus();  // Perbarui status bar setelah reset game
    }

    public void setCell(int row, int col, int value) {
        if (cells[row][col].isEditable()) {
            boolean isCorrect = (value == puzzle.numbers[row][col]);
            cells[row][col].setValue(value);
            cells[row][col].validateValue(isCorrect);


            if (isCorrect) {
                sudokuMain.playSound("src/Audio/true.wav"); // Saat pemain memasukkan angka benar
            } else {
                sudokuMain.playSound("src/Audio/wrong.wav"); // Saat pemain memasukkan angka salah
            }

            sudokuMain.updateStatus();
        }
    }

    public void highlightCell(int row, int col) {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                cells[i][j].setHighlighted(i == row && j == col);
            }
        }
        sudokuMain.updateStatus();  // Perbarui status bar setelah highlight
    }

    // Metode menghitung kolom yang tersisa
    public int getRemainingCells() {
        int count = 0;
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (cells[row][col].isEditable() && cells[row][col].getValue() == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isBoardComplete() {
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (cells[row][col].getValue() == 0) {
                    return false;
                }
            }
        }
        if (isValidSolution()) {
            sudokuMain.playSound("src/Audio/win.wav"); // Suara kemenangan
            return true;
        }
        return false;
    }

    private boolean isValidSolution() {
        for (int row = 0; row < GRID_SIZE; ++row) {
            boolean[] seen = new boolean[GRID_SIZE + 1];
            for (int col = 0; col < GRID_SIZE; ++col) {
                int value = cells[row][col].getValue();
                if (value < 1 || value > GRID_SIZE || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        for (int col = 0; col < GRID_SIZE; ++col) {
            boolean[] seen = new boolean[GRID_SIZE + 1];
            for (int row = 0; row < GRID_SIZE; ++row) {
                int value = cells[row][col].getValue();
                if (value < 1 || value > GRID_SIZE || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        for (int startRow = 0; startRow < GRID_SIZE; startRow += 3) {
            for (int startCol = 0; startCol < GRID_SIZE; startCol += 3) {
                boolean[] seen = new boolean[GRID_SIZE + 1];
                for (int row = startRow; row < startRow + 3; ++row) {
                    for (int col = startCol; col < startCol + 3; ++col) {
                        int value = cells[row][col].getValue();
                        if (value < 1 || value > GRID_SIZE || seen[value]) {
                            return false;
                        }
                        seen[value] = true;
                    }
                }
            }
        }

        return true;
    }

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            int value = sourceCell.getValue();
            if (value == 0) return;

            // Validasi input pemain
            setCell(sourceCell.getRow(), sourceCell.getCol(), value);

            if (isBoardComplete()) {
                JOptionPane.showMessageDialog(sudokuMain, "Congratulations! You have completed the Sudoku puzzle!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
