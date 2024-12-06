/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231159 - Mohammad Ferdinand Valliandra
 * 2 - 5026231135 - Fachreza Aptadhi Kurniawan
 * 3 - 5026231149 - Ananda Donelly Reksana
 */

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
import javax.sound.sampled.*; // Import Sound Effect
import java.io.File;
import java.io.IOException;

public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;
    private GameBoardPanel board;
    private JTextField statusBar;

    // Deklarasi Timer
    private Timer gameTimer;
    private int elapsedTime = 0;

    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        board = new GameBoardPanel(this);
        statusBar = new JTextField("Welcome to Sudoku!");
        statusBar.setEditable(false);

        cp.add(board, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.SOUTH);

        setupMenuBar();
        setupKeyboardNavigation();
        setupWelcomeScreen();

        board.newGame();
        startTimer();
        updateStatus();

        setAppLogo();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku Game");
        setVisible(true);
    }

    // Metode untuk memutar Sound Effect
    public void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void setupKeyboardNavigation() {
        board.addKeyListener(new KeyAdapter() {
            private int currentRow = 0;
            private int currentCol = 0;

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> currentRow = Math.max(0, currentRow - 1);
                    case KeyEvent.VK_DOWN -> currentRow = Math.min(8, currentRow + 1);
                    case KeyEvent.VK_LEFT -> currentCol = Math.max(0, currentCol - 1);
                    case KeyEvent.VK_RIGHT -> currentCol = Math.min(8, currentCol + 1);
                    case KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4,
                         KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8,
                         KeyEvent.VK_9 -> {
                        int value = e.getKeyCode() - KeyEvent.VK_0;
                        board.setCell(currentRow, currentCol, value);
                        if (board.isBoardComplete()) {
                            stopTimer();
                            playSound("src/Audio/win.wav");
                            JOptionPane.showMessageDialog(SudokuMain.this, "Congratulations! You have completed the Sudoku puzzle!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                board.highlightCell(currentRow, currentCol);
                updateStatus();
            }
        });
        board.setFocusable(true);
        board.requestFocusInWindow();
    }

    // Mengganti Icon Aplikasi
    private void setAppLogo() {
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource("/Image/logo_sudoku.png"));
            setIconImage(logo.getImage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Logo tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menu Bar dan Menu File
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Menu Item Reset Game
        JMenuItem resetGameItem = new JMenuItem("Reset Game");
        resetGameItem.addActionListener(e -> resetGameWithConfirmation());
        fileMenu.add(resetGameItem);

        // Menu Item New Game
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            board.newGame();
            resetTimer(); // Reset timer ketika klik New Game
        });
        fileMenu.add(newGameItem);

        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    // Metode Reset Game
    private void resetGameWithConfirmation() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to reset the game? All progress will be lost.",
                "Reset Game",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.OK_OPTION) {
            board.resetGame(); // Panggil fungsi reset
            resetTimer();
            updateStatus(); // Perbarui status bar setelah reset game
        }
    }

    // Fitur Welcome Screen
    private void setupWelcomeScreen() {
        JDialog welcomeDialog = new JDialog(this, "Welcome to Sudoku!", true);
        welcomeDialog.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Sudoku!", JLabel.CENTER);
        JButton btnSinglePlayer = new JButton("Play Now");

        btnSinglePlayer.addActionListener(e -> welcomeDialog.dispose());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        buttonPanel.add(btnSinglePlayer);

        welcomeDialog.add(welcomeLabel, BorderLayout.CENTER);
        welcomeDialog.add(buttonPanel, BorderLayout.SOUTH);

        welcomeDialog.setSize(400, 200);
        welcomeDialog.setLocationRelativeTo(null);
        welcomeDialog.setVisible(true);
    }

    // Menampilkan metode menghitung kolom yang tersisa
    public void updateStatus() {
        statusBar.setText(board.getRemainingCells() + " cells remaining. Time: " + elapsedTime + "s");
    }

    // Inisialisasi Timer
    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            elapsedTime++;
            updateStatus();
        });
        gameTimer.start(); // Timer dimulai saat game dimulai
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    private void resetTimer() {
        stopTimer();
        elapsedTime = 0;
        startTimer();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuMain::new);
    }
}
