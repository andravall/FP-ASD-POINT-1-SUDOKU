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

public class Cell extends JTextField {
    private static final long serialVersionUID = 1L;
    private int row, col;
    private boolean editable;
    private int value; // Menambahkan atribut value untuk menyimpan nilai sel
    private boolean isGiven; // Menambahkan atribut isGiven untuk status "given"

    public Cell(int row, int col) {
        super(1);
        this.row = row;
        this.col = col;
        this.editable = true; // Secara default, setiap sel bisa diedit
        this.value = 0; // Default value adalah 0 (kosong)
        this.isGiven = false; // Secara default, sel bukan "given"

        setHorizontalAlignment(JTextField.CENTER);
        setFont(new Font("Arial", Font.BOLD, 20));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Event listener untuk menangani input pengguna
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c < '1' || c > '9') {
                    e.consume(); // hanya menerima input angka 1-9
                }
            }
        });
    }

    // Getter untuk baris dan kolom
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // Getter dan setter untuk properti editable
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        super.setEditable(editable); // Panggil method JTextField untuk mengatur editable
        setBackground(editable ? Color.WHITE : Color.LIGHT_GRAY);
    }

    // Method untuk memulai permainan baru
    public void newGame(int value, boolean isGiven) {
        this.value = value; // Set nilai awal untuk sel
        this.isGiven = isGiven; // Tandai apakah sel ini adalah "given"
        setEditable(!isGiven); // Jika "given", sel tidak dapat diedit
        setText(isGiven ? String.valueOf(value) : ""); // Tampilkan nilai hanya jika "given"
        setBackground(isGiven ? Color.LIGHT_GRAY : Color.WHITE); // Atur warna latar sesuai status "given"
    }

    // Setter untuk nilai sel
    public void setValue(int value) {
        this.value = value; // Update nilai sel
        if (isEditable()) {
            setText(value == 0 ? "" : String.valueOf(value)); // Kosongkan jika nilai 0
        }
    }

    // Getter untuk nilai sel
    public int getValue() {
        try {
            return Integer.parseInt(getText()); // Ambil nilai dari teks di sel
        } catch (NumberFormatException e) {
            return 0; // Jika teks kosong atau bukan angka
        }
    }

    // Reset sel ke keadaan awal
    public void reset() {
        value = 0; // Set nilai sel ke 0 (kosong)
        setText(""); // Kosongkan teks di sel
        setBackground(Color.WHITE); // Atur warna background kembali ke putih
    }

    // Highlight sel
    public void setHighlighted(boolean highlighted) {
        setBackground(highlighted ? Color.YELLOW : (editable ? Color.WHITE : Color.LIGHT_GRAY));
    }

    // Validasi nilai sel dan mengganti background ketika benar(hijau) dan salah(merah)
    public void validateValue(boolean isCorrect) {
        if (isEditable()) {
            setBackground(isCorrect ? Color.GREEN : Color.RED);
        }
    }
}

