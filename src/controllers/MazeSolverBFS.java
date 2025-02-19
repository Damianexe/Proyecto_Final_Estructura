package controllers;

import java.util.*;

import controllers.interfaces.MazeSolver;
import models.Cell;
import models.Maze;

public class MazeSolverBFS implements MazeSolver {

    @Override
    public List<Cell> getPath(Maze maze, Cell start, Cell end, Set<Cell> blockedCells) {
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> predecesores = new HashMap<>();
        List<Cell> path = new ArrayList<>();
        Set<Cell> visited = new HashSet<>();
        boolean[][] grid = maze.getGrid();
        
        queue.add(start);
        predecesores.put(start, null);
        visited.add(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            maze.updateMaze(current, start, end);

            if (current.equals(end)) {
                while (current != null) {
                    path.add(0, current);
                    current = predecesores.get(current);
                }
                return path;
            }

            int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };

            for (int[] dir : directions) {
                int newRow = current.getRow() + dir[0];
                int newCol = current.getCol() + dir[1];
                Cell neighbor = new Cell(newRow, newCol);

                if (isValid(grid, newRow, newCol, blockedCells) && !visited.contains(neighbor)) {
                    queue.add(neighbor);
                    predecesores.put(neighbor, current);
                    visited.add(neighbor);
                }
                
                
            }
        }
        return new ArrayList<>();
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
