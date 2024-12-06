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

/**
 * The Sudoku number puzzle to be solved
 */
public class Puzzle {
    public int[][] numbers = new int[9][9];
    public boolean[][] isGiven = new boolean[9][9];

    public void newPuzzle(int clues) {
        int[][] sample = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        for (int row = 0; row < 9; row++) {
            System.arraycopy(sample[row], 0, numbers[row], 0, 9);
        }

        // Randomize clues
        for (int i = 0; i < 81; ++i) {
            int row = i / 9;
            int col = i % 9;
            isGiven[row][col] = Math.random() * 81 < clues;
        }
    }
}
