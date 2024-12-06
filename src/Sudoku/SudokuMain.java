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

import javax.swing.*;

public class SudokuMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sudoku");
            GameBoardPanel boardPanel = new GameBoardPanel();
            frame.setContentPane(boardPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            boardPanel.newGame();
        });
    }
}
