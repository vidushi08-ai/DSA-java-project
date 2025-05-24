import java.util.*;

public class SudokuGame {
    private static final int SIZE = 9;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static long startTime;

    public static void main(String[] args) {
        while (true) {
            System.out.println("Select Difficulty: [1] Easy  [2] Medium  [3] Hard");
            int choice = scanner.nextInt();
            int[][] puzzle = generatePuzzle(choice);
            int[][] original = copyBoard(puzzle);

            startTime = System.currentTimeMillis();

            while (true) {
                printBoard(puzzle);
                System.out.println("[1] Enter a number");
                System.out.println("[2] Solve");
                System.out.println("[3] Reset");
                System.out.println("[4] Exit");

                int option = scanner.nextInt();

                if (option == 1) {
                    System.out.print("Enter row (0-8): ");
                    int row = scanner.nextInt();
                    System.out.print("Enter column (0-8): ");
                    int col = scanner.nextInt();
                    System.out.print("Enter number (1-9): ");
                    int num = scanner.nextInt();

                    if (original[row][col] != 0) {
                        System.out.println("❌ You cannot change original puzzle values!");
                    } else if (!isValid(puzzle, row, col, num)) {
                        System.out.println("❌ Invalid move!");
                    } else {
                        puzzle[row][col] = num;
                    }

                } else if (option == 2) {
                    if (solveSudoku(puzzle)) {
                        printBoard(puzzle);
                        long endTime = System.currentTimeMillis();
                        long timeTaken = (endTime - startTime) / 1000;
                        int score = calculateScore(choice, timeTaken);
                        System.out.println("🎉 Sudoku Solved!");
                        System.out.println("⏱ Time: " + timeTaken + " seconds");
                        System.out.println("🏆 Score: " + score);
                        break;
                    } else {
                        System.out.println("❌ No solution exists!");
                    }

                } else if (option == 3) {
                    puzzle = copyBoard(original);
                    startTime = System.currentTimeMillis();
                    System.out.println("🔁 Puzzle reset.");
                } else if (option == 4) {
                    System.out.println("Goodbye!");
                    return;
                } else {
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    // Generate puzzle based on difficulty
    private static int[][] generatePuzzle(int level) {
        int[][] full = new int[SIZE][SIZE];
        solveSudoku(full);  // fill board with solved puzzle

        int clues;
        if (level == 1) clues = 40;
        else if (level == 2) clues = 30;
        else clues = 20;

        int[][] puzzle = copyBoard(full);

        // Remove cells
        int removed = SIZE * SIZE - clues;
        while (removed > 0) {
            int r = random.nextInt(SIZE);
            int c = random.nextInt(SIZE);
            if (puzzle[r][c] != 0) {
                puzzle[r][c] = 0;
                removed--;
            }
        }
        return puzzle;
    }

    private static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int i = boxRowStart; i < boxRowStart + 3; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }
        return true;
    }

    private static void printBoard(int[][] board) {
        System.out.println("   0 1 2   3 4 5   6 7 8");
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("   ------+-------+------");
            System.out.print(i + "  ");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");
                System.out.print((board[i][j] == 0 ? "." : board[i][j]) + " ");
            }
            System.out.println();
        }
    }

    private static int[][] copyBoard(int[][] original) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    private static int calculateScore(int level, long time) {
        int base = (level == 1) ? 500 : (level == 2) ? 1000 : 1500;
        long bonus = Math.max(0, 300 - time);
        return (int) (base + bonus * 5);
    }
}
