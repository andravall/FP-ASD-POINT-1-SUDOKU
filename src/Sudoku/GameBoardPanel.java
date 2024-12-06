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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int CELL_SIZE = 60;
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();

    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        CellInputListener listener = new CellInputListener();

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }
        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        // Set garis tebal untuk subgrid
        g2d.setStroke(new BasicStroke(3));

        // Gambar garis subgrid horizontal
        for (int row = 1; row < SudokuConstants.SUBGRID_SIZE; ++row) {
            int y = row * CELL_SIZE * SudokuConstants.SUBGRID_SIZE;
            g2d.drawLine(0, y, BOARD_WIDTH, y);
        }

        // Gambar garis subgrid vertikal
        for (int col = 1; col < SudokuConstants.SUBGRID_SIZE; ++col) {
            int x = col * CELL_SIZE * SudokuConstants.SUBGRID_SIZE;
            g2d.drawLine(x, 0, x, BOARD_HEIGHT);
        }
    }

    public void newGame() {
        puzzle.newPuzzle(2);

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();

            try {
                int numberIn = Integer.parseInt(sourceCell.getText().trim());
                System.out.println("You entered " + numberIn);

                if (numberIn == sourceCell.number) {
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                } else {
                    sourceCell.status = CellStatus.WRONG_GUESS;
                }
                sourceCell.paint();

                if (isSolved()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle!");
                }

            } catch (NumberFormatException ex) {
                System.err.println("Invalid input: Please enter a valid integer.");
                sourceCell.setText("");
            }
        }
    }
}