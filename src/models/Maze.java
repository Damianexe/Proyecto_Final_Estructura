package models;

import java.util.Arrays;

public class Maze {
    private boolean[][] grid;
    private final int rows, cols;

    // Constructor con tamaño personalizado
    public Maze(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Las dimensiones del laberinto deben ser mayores a 0.");
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new boolean[rows][cols];

        // Inicializar todas las celdas como transitables (true)
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], true);
        }
    }

    //  Método para actualizar el estado del laberinto (usado en la UI)
    public void updateMaze(Cell cell, Cell start, Cell end) {
        if (cell.equals(start)) {
            System.out.println("Inicio: " + cell.getRow() + ", " + cell.getCol());
        } else if (cell.equals(end)) {
            System.out.println("Fin: " + cell.getRow() + ", " + cell.getCol());
        } else {
            System.out.println("Explorando: " + cell.getRow() + ", " + cell.getCol());
        }
    }

    //  Método para obtener la matriz del laberinto
    public boolean[][] getGrid() {
        return grid;
    }

    //  Método para verificar si una celda es transitable con validación de índices
    public boolean isCellOpen(int row, int col) {
        if (!isValidCell(row, col)) {
            return false;
        }
        return grid[row][col];
    }

    //  Método para modificar una celda (hacerla transitable o bloqueada)
    public void setCellState(int row, int col, boolean state) {
        if (isValidCell(row, col)) {
            grid[row][col] = state;
        }
    }

    //  Método para verificar si una celda está dentro del rango válido
    public boolean isValidCell(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }

    //  Getters para las dimensiones del laberinto
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
