package controllers.interfaces;

import java.util.List;
import java.util.Set;

import models.Cell;
import models.Maze;

public interface MazeSolver {
    List<Cell> getPath(Maze maze, Cell start, Cell end, Set<Cell> blockedCells);
}
