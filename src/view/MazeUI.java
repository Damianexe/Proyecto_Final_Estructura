package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controllers.*;
import controllers.interfaces.MazeSolver;
import models.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class MazeUI extends JFrame {
    private Maze maze;
    private JPanel gridPanel;
    private JButton solveBFS, solveDFS, solveDP, solveRecursive, generateMazeButton, setStartButton, setEndButton, resetButton;
    private JTextArea outputArea;
    private JTextField rowsField, colsField;
    private int rows = 10, cols = 10;
    private JLabel[][] laberintoLabels;
    private Cell startCell, endCell;
    private Set<Cell> blockedCells = new HashSet<>();

    public MazeUI() {
        setTitle("Maze Solver");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new FlowLayout());

        rowsField = new JTextField("10", 5);
        colsField = new JTextField("10", 5);
        generateMazeButton = new JButton("Generar Laberinto");
        setStartButton = new JButton("Seleccionar Inicio");
        setEndButton = new JButton("Seleccionar Fin");
        resetButton = new JButton("Reiniciar Todo");

        configPanel.add(new JLabel("Filas:"));
        configPanel.add(rowsField);
        configPanel.add(new JLabel("Columnas:"));
        configPanel.add(colsField);
        configPanel.add(generateMazeButton);
        configPanel.add(setStartButton);
        configPanel.add(setEndButton);
        configPanel.add(resetButton);

        gridPanel = new JPanel();

        JPanel buttonPanel = new JPanel();
        solveBFS = new JButton("Solve BFS");
        solveDFS = new JButton("Solve DFS");
        solveDP = new JButton("Solve DP");
        solveRecursive = new JButton("Solve Recursive");

        buttonPanel.add(solveBFS);
        buttonPanel.add(solveDFS);
        buttonPanel.add(solveDP);
        buttonPanel.add(solveRecursive);

        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);

        add(configPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JScrollPane(outputArea), BorderLayout.EAST);

        generateMazeButton.addActionListener(e -> generateMaze());
        setStartButton.addActionListener(e -> selectStartCell());
        setEndButton.addActionListener(e -> selectEndCell());
        resetButton.addActionListener(e -> resetMaze());
        solveBFS.addActionListener(e -> solveMaze(new MazeSolverBFS()));
        solveDFS.addActionListener(e -> solveMaze(new MazeSolverDFS()));
        solveDP.addActionListener(e -> solveMaze(new MazeSolverDP()));
        solveRecursive.addActionListener(e -> solveMaze(new MazeSolverRecursivo()));
    }

    private void generateMaze() {
        try {
            rows = Integer.parseInt(rowsField.getText());
            cols = Integer.parseInt(colsField.getText());
            maze = new Maze(rows, cols);
            initializeGrid();
            startCell = null;
            endCell = null;
            blockedCells.clear();
            outputArea.setText("Laberinto generado con " + rows + " filas y " + cols + " columnas.\n");
        } catch (NumberFormatException e) {
            outputArea.setText("Error: Ingresa números válidos para filas y columnas.\n");
        }
    }

    private void clearPath() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!isCellBlocked(i, j) && !new Cell(i, j).equals(startCell) && !new Cell(i, j).equals(endCell)) {
                    laberintoLabels[i][j].setBackground(Color.WHITE); // Restaurar solo celdas transitables
                }
            }
        }
    }
    
    private boolean isCellBlocked(int row, int col) {
        return blockedCells.contains(new Cell(row, col));
    }

    
    private void solveMaze(MazeSolver solver) {
        if (startCell == null || endCell == null) {
            outputArea.setText("Error: Selecciona el punto de inicio y fin.\n");
            return;
        }
    
        clearPath(); //  Borra solo el camino anterior sin tocar inicio y fin
    
        outputArea.setText("Resolviendo laberinto usando " + solver.getClass().getSimpleName() + "...\n");
        List<Cell> path = solver.getPath(maze, startCell, endCell, blockedCells);
    
        if (path != null && !path.isEmpty()) {
            for (Cell c : path) {
                if (!isCellBlocked(c.getRow(), c.getCol())) { 
                    laberintoLabels[c.getRow()][c.getCol()].setBackground(Color.GREEN);
                }
            }
            outputArea.append("Camino encontrado con " + path.size() + " pasos.\n");
        } else {
            outputArea.append("No se encontró un camino.\n");
        }
    }
    
    

    private void selectStartCell() {
        outputArea.setText("Selecciona una celda de inicio en el laberinto.");
    }

    private void selectEndCell() {
        outputArea.setText("Selecciona una celda de fin en el laberinto.");
    }

    private void initializeGrid() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(rows, cols));
        laberintoLabels = new JLabel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JLabel cellLabel = new JLabel("", SwingConstants.CENTER);
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellLabel.setOpaque(true);
                cellLabel.setBackground(Color.WHITE);
                final int r = i, c = j;
                cellLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (startCell == null) {
                            startCell = new Cell(r, c);
                            cellLabel.setBackground(Color.BLUE);
                        } else if (endCell == null) {
                            endCell = new Cell(r, c);
                            cellLabel.setBackground(Color.RED);
                        } else {
                            if (blockedCells.contains(new Cell(r, c))) {
                                blockedCells.remove(new Cell(r, c));
                                cellLabel.setBackground(Color.WHITE);
                            } else {
                                blockedCells.add(new Cell(r, c));
                                cellLabel.setBackground(Color.BLACK);
                            }
                        }
                    }
                });
                laberintoLabels[i][j] = cellLabel;
                gridPanel.add(cellLabel);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void resetMaze() {
        generateMaze();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MazeUI().setVisible(true));
    }
}
