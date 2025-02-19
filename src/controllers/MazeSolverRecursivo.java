package controllers;

import java.util.*;

import controllers.interfaces.MazeSolver;
import models.Cell;
import models.Maze;

public class MazeSolverRecursivo implements MazeSolver {

    @Override
    public List<Cell> getPath(Maze maze, Cell start, Cell end, Set<Cell> blockedCells) {
        List<Cell> path = new ArrayList<>();
        Set<Cell> visitadas = new LinkedHashSet<>();

        if (findPath(maze, maze.getGrid(), start.getRow(), start.getCol(), end, path, visitadas, blockedCells)) {
            return path;
        }

        return new ArrayList<>();
    }

    private boolean findPath(Maze maze, boolean[][] grid, int row, int col, Cell end, List<Cell> path, Set<Cell> visitadas, Set<Cell> blockedCells) {
        //  Verificar si la celda es válida antes de crear un objeto Cell
        if (!isValid(grid, row, col, blockedCells)) {
            return false;
        }

        Cell cell = new Cell(row, col);

        //  Si llegamos al destino, terminamos la búsqueda
        if (cell.equals(end)) {
            path.add(cell);
            return true;
        }

        //  Si la celda ya fue visitada, no continuar
        if (!visitadas.add(cell)) {
            return false;
        }

        maze.updateMaze(cell, path.isEmpty() ? cell : path.get(0), end);

        //  Explorar en las direcciones óptimas
        int[][] direcciones = obtenerDireccionesOptimas(row, col, end);

        for (int[] dir : direcciones) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (findPath(maze, grid, newRow, newCol, end, path, visitadas, blockedCells)) {
                path.add(cell);  // Asegura que la celda se agregue correctamente al camino
                return true;
            }
            
            
        }

        //  Si no es parte del camino correcto, hacer backtracking
        path.remove(cell);
        return false;
    }

    /**
     * Ordena las direcciones de exploración para priorizar la ruta más corta.
     */
    private int[][] obtenerDireccionesOptimas(int row, int col, Cell end) {
        List<int[]> direcciones = new ArrayList<>(Arrays.asList(
            new int[]{1, 0},  // Abajo
            new int[]{0, 1},  // Derecha
            new int[]{0, -1}, // Izquierda
            new int[]{-1, 0}  // Arriba
        ));

        // Ordenar en base a la distancia Manhattan al destino
        direcciones.sort(Comparator.comparingInt(d -> 
            Math.abs((row + d[0]) - end.getRow()) + Math.abs((col + d[1]) - end.getCol())
        ));

        return direcciones.toArray(new int[0][0]);
    }

    private boolean isValid(boolean[][] grid, int row, int col, Set<Cell> blockedCells) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return false; // Está fuera de los límites
        }
        if (!grid[row][col]) {
            return false; // Celda no transitable
        }
        if (blockedCells.contains(new Cell(row, col))) {
            return false; // Celda bloqueada
        }
        return true;
    }
    
}
