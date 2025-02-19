package controllers;

import java.util.*;

import controllers.interfaces.MazeSolver;
import models.Cell;
import models.Maze;

public class MazeSolverDFS implements MazeSolver {

    @Override
    public List<Cell> getPath(Maze maze, Cell start, Cell end, Set<Cell> blockedCells) {
        List<Cell> path = new ArrayList<>();
        Set<Cell> visited = new HashSet<>();
        boolean[][] grid = maze.getGrid();
        
        if (dfs(maze, grid, start.getRow(), start.getCol(), end, path, visited, blockedCells))
            return path;
        
        return new ArrayList<>();
    }

    private boolean dfs(Maze maze, boolean[][] grid, int row, int col, Cell end, List<Cell> path, Set<Cell> visited, Set<Cell> blockedCells) {
        //  Verificar si la celda es válida antes de crear un objeto Cell
        if (!isValid(grid, row, col, blockedCells)) {
            return false;
        }

        Cell cell = new Cell(row, col);
        if (visited.contains(cell)) {
            return false;
        }

        visited.add(cell);
        path.add(cell);
        maze.updateMaze(cell, path.get(0), end); 

        //  Si llegamos al destino, terminamos la búsqueda
        if (cell.equals(end)) {
            return true;
        }

        //  Explorar en las 4 direcciones (Abajo, Derecha, Izquierda, Arriba)
        int[][] directions = { {1, 0}, {0, 1}, {0, -1}, {-1, 0} };

        for (int[] dir : directions) {
            if (dfs(maze, grid, row + dir[0], col + dir[1], end, path, visited, blockedCells)) {
                return true;
            }
        }

        //  Si no es parte del camino correcto, hacer backtracking
        path.remove(cell);
        return false;
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
