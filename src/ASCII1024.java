import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ASCII1024 {
    private static final int MIN_GRID_SIZE = 4;
    private static final int MAX_GRID_SIZE = 10;
    private static final int WIN_VALUE = 1024;
    private static final int INITIAL_VALUE = 1;
    private static final char UP = 'W';
    private static final char DOWN = 'S';
    private static final char LEFT = 'A';
    private static final char RIGHT = 'D';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Get the grid size from the user
        int gridSize = getGridSize(scanner);
        int[][] grid = new int[gridSize][gridSize];

        // Add two initial tiles to the grid
        addRandomTile(grid, random, INITIAL_VALUE);
        addRandomTile(grid, random, INITIAL_VALUE);

        int score = 0;
        boolean gameWon = false;

        // Main game loop
        while (!gameWon) {
            // Display the grid and input instructions
            drawGrid(grid);
            System.out.println("Enter a direction to move tiles (W = Up, S = Down, A = Left, D = Right): ");

            // Get the user's move
            char move = getMove(scanner);

            // Try to move the tiles in the selected direction
            boolean moved = moveTiles(grid, move);

            // If no tiles moved or merged, let the user try again
            if (!moved) {
                continue;
            }

            // Check if the user won
            gameWon = checkWin(grid, WIN_VALUE);
            if (gameWon) {
                System.out.println("Congratulations, you won!");
            }

            // Check if there are any empty tiles left
            boolean hasEmptyTiles = hasEmptyTiles(grid);
            if (!hasEmptyTiles) {
                break;
            }

            // Add a new tile to the grid
            addRandomTile(grid, random, INITIAL_VALUE);
        }

        // Game over
        System.out.println("Game over. Your final score is: " + score);
    }

    // Prompts the user to enter the grid size and returns it
    private static int getGridSize(Scanner scanner) {
        while (true) {
            System.out.println("Enter the size of the grid (between " + MIN_GRID_SIZE + " and " + MAX_GRID_SIZE + "): ");
            int size = scanner.nextInt();
            if (size >= MIN_GRID_SIZE && size <= MAX_GRID_SIZE) {
                return size;
            }
            System.out.println("Invalid size. Please try again.");
        }
    }
    // Adds a new tile with the given value to a random empty cell in the grid
    private static void addRandomTile(int[][] grid, Random random, int value) {
        // Create a list of empty cells
        ArrayList<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0) {
                    emptyCells.add(new int[] {i, j});
                }
            }
        }

        // Choose a random empty cell and add the new tile
        int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
        grid[cell[0]][cell[1]] = value;
    }

    // Draws the grid using ASCII characters
    private static void drawGrid(int[][] grid) {
        // Calculate the width of each cell
        int cellWidth = String.valueOf(grid[grid.length - 1][grid.length - 1]).length();

        // Print the top border
        for (int i = 0; i < grid.length; i++) {
            System.out.print("+");
            for (int j = 0; j < cellWidth; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");

        // Print the cells
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print("|");
                System.out.printf("%-" + cellWidth + "d", grid[i][j]);
            }
            System.out.println("|");
        }

        // Print the bottom border
        for (int i = 0; i < grid.length; i++) {
            System.out.print("+");
            for (int j = 0; j < cellWidth; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    // Gets the user's move
    private static char getMove(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            if (input.length() == 1) {
                char move = input.charAt(0);
                if (move == UP || move == DOWN || move == LEFT || move == RIGHT) {
                    return move;
                }
            }
            System.out.println("Invalid input. Please try again.");
        }
    }

    // Tries to move the tiles in the given direction and returns true if any tiles moved or merged
    private static boolean moveTiles(int[][] grid, char move) {
        boolean moved = false;

        int score =0;
        if (move == UP) {
            // Iterate over each column
            for (int j = 0; j < grid.length; j++) {
                // Iterate over each row in reverse order
                for (int i = grid.length - 1; i >= 0; i--) {
                    // Skip empty cells
                    if (grid[i][j] == 0) {
                        continue;
                    }
                    // Try to move the tile up
                    int row = i - 1;
                    while (row >= 0) {
                        if (grid[row][j] == 0) {
                            // Move the tile up
                            grid[row][j] = grid[row + 1][j];
                            grid[row + 1][j] = 0;
                            moved = true;
                        } else if (grid[row][j] == grid[row + 1][j]) {
                            // Merge the tiles and update the score
                            grid[row][j] *= 2;
                            grid[row + 1][j] = 0;
                            score += grid[row][j];
                            moved = true;
                            break;
                        } else {
                            // Stop moving the tile
                            break;
                        }
                        row--;
                    }
                }
            }
        } else if (move == DOWN) {
            // Iterate over each column
            for (int j = 0; j < grid.length; j++) {
                // Iterate over each row
                for (int i = 0; i < grid.length; i++) {
                    // Skip empty cells
                    if (grid[i][j] == 0) {
                        continue;
                    }
                    // Try to move the tile down
                    int row = i + 1;
                    while (row < grid.length) {
                        if (grid[row][j] == 0) {
                            // Move the tile down
                            grid[row][j] = grid[row - 1][j];
                            grid[row - 1][j] = 0;
                            moved = true;
                        } else if (grid[row][j] == grid[row - 1][j]) {
                            // Merge the tiles and update the score
                            grid[row][j] *= 2;
                            grid[row - 1][j] = 0;
                            score += grid[row][j];
                            moved = true;
                            break;
                        } else {
                            // Stop moving the tile
                            break;
                        }
                        row++;
                    }
                }
            }
        } else if (move == LEFT) {
            // Iterate over each row
            for (int i = 0; i < grid.length; i++) {
                // Iterate over each column in reverse order
                for (int j = grid.length - 1; j >= 0; j--) {
                    // Skip empty cells
                    if (grid[i][j] == 0) {
                        continue;
                    }
                    // Try to move the tile left
                    int col = j - 1;
                    while (col >= 0) {
                        if (grid[i][col] == 0) {
                            // Move the tile left
                            grid[i][col] = grid[i][col + 1];
                            grid[i][col + 1] = 0;
                            moved = true;
                        } else if (grid[i][col] == grid[i][col + 1]) {
                            // Merge the tiles and update the score
                            grid[i][col] *= 2;
                            grid[i][col + 1] = 0;
                            score += grid[i][col];
                            moved = true;
                            break;
                        } else {
                            // Stop moving the tile
                            break;
                        }
                        col--;
                    }
                }
            }
        } else if (move == RIGHT) {
            // Iterate over each row
            for (int i = 0; i < grid.length; i++) {
                // Iterate over each column
                for (int j = 0; j < grid.length; j++) {
                    // Skip empty cells
                    if (grid[i][j] == 0) {
                        continue;
                    }
                    // Try to move the tile right
                    int col = j + 1;
                    while (col < grid.length) {
                        if (grid[i][col] == 0) {
                            // Move the tile right
                            grid[i][col] = grid[i][col - 1];
                            grid[i][col - 1] = 0;
                            moved = true;
                        } else if (grid[i][col] == grid[i][col - 1]) {
                            // Merge the tiles and update the score
                            grid[i][col] *= 2;
                            grid[i][col - 1] = 0;
                            score += grid[i][col];
                            moved = true;
                            break;
                        } else {
                            // Stop moving the tile
                            break;
                        }
                        col++;
                    }
                }
            }
        }

        return moved;
    }

// Checks if the grid contains a tile with the given value and returns true if it does
private static boolean checkWin(int[][] grid, int value) {
    for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[i].length; j++) {
            if (grid[i][j] == value) {
                return true;
            }
        }
    }
    return false;
}

    // Returns true if the grid has any empty cells, false otherwise
    private static boolean hasEmptyTiles(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}