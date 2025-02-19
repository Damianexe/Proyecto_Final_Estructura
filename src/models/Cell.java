package models;

import java.util.Objects;

public class Cell implements Comparable<Cell> {

    private final int row, col;

    public Cell(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Las coordenadas no pueden ser negativas.");
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Cell cell = (Cell) obj;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    @Override
    public int compareTo(Cell other) {
        // Ordena por fila y luego por columna
        return row != other.row ? Integer.compare(row, other.row) : Integer.compare(col, other.col);
    }
}
