package Puzzles;

import java.util.*;

/* 
 * This class represents nXn puzzle, according to the original 15-puzzle logic.
 * A Puzzle can be initialized by perforimg n random moves from the solution board,
 * or with a custom starting board.
 * Each Puzzle have the ability to perform move, check if its solved
 * and generate the possible board state from the current board.
 * 
 * @author: Aviv Cohen
 * 
 */

public class Puzzle {
    private final int size;
    private final int[][] board;
    private int emptyRow;
    private int emptyCol;
    // Moves
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    public Puzzle(int size, int[][] startingBoard) {
        // Initial with starting board
        this.size = size;
        if (isValidBoard(startingBoard)) {
            this.board = new int[size][size];
            initializeBoard(startingBoard);
        } else {
            throw new IllegalArgumentException("Invalid starting board");
        }
    }

    public Puzzle(int size, int n) {
        // Initial starting board by perforimg random moves (n) from the solution board
        this.size = size;
        this.board = new int[size][size];
        initializeRandomBoard(n);
    }

    public Puzzle(Puzzle other) {
        // Copy constructor
        this.size = other.size;
        this.board = new int[size][size];
        this.emptyRow = other.emptyRow;
        this.emptyCol = other.emptyCol;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = other.board[i][j];
            }
        }
    }

    public int size() {
        return this.size;
    }

    public int[][] getBoard() {
        // Return a copy of the puzzle board to avoid external modification
        int size = board.length;
        int[][] copy = new int[size][];
        for (int i = 0; i < size; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }

    private boolean isValidBoard(int[][] board) {
        // Check if the board has the correct size
        if (board.length != size || board[0].length != size) {
            return false;
        }

        // Flatten the 2D board to a 1D array for permutation checking
        int[] flatBoard = new int[size * size];
        int k = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                flatBoard[k++] = board[i][j];
            }
        }

        // Check if the board contains unique numbers from 0 to size*size - 1
        boolean[] visited = new boolean[size * size];

        for (int num : flatBoard) {
            if (num < 0 || num >= size * size || visited[num]) {
                return false; // Invalid number or duplicate
            }
            visited[num] = true;
        }

        // Check if the permutation is solvable
        return isSolvable(flatBoard);
    }

    private boolean isSolvable(int[] flatBoard) {
        // Helper method to check if the permutation is solvable
        int inversions = 0;

        for (int i = 0; i < flatBoard.length - 1; i++) {
            for (int j = i + 1; j < flatBoard.length; j++) {
                if (flatBoard[i] > flatBoard[j] && flatBoard[i] != 0 && flatBoard[j] != 0) {
                    inversions++;
                }
            }
        }

        // For a 4x4 board, check the row number of the empty space
        int emptyRow = 0;
        for (int i = 0; i < flatBoard.length; i++) {
            if (flatBoard[i] == 0) {
                emptyRow = size - 1 - i / size; // Fix the calculation here
                break;
            }
        }

        // The puzzle is solvable if the number of inversions is even or if the empty
        // space
        // is on an even row counting from the bottom (1-based index)
        return (inversions % 2 == 0 && size % 2 == 1) || ((inversions + emptyRow) % 2 == 0 && size % 2 == 0);
    }

    public boolean isSolved() {
        int count = 1;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) {
                    // Check if the last position is empty (0)
                    if (board[i][j] != 0) {
                        return false;
                    }
                } else {
                    // Check if the other positions contain consecutive numbers
                    if (board[i][j] != count) {
                        return false;
                    }
                    count = (count + 1) % (size * size);
                }
            }
        }
        return true;
    }

    private void initializeBoard(int[][] startingBoard) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = startingBoard[i][j];
                if (board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    private void initializeRandomBoard(int n) {
        // Initialize the board to the solved state
        int count = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = count++;
            }
        }
        board[size - 1][size - 1] = 0; // Set the last element to 0, representing the empty space
        emptyRow = size - 1;
        emptyCol = size - 1;

        // Perform valid random moves (n) on the board
        if (n > 0) {
            makeRandomMoves(n);
        }
    }

    private void makeRandomMoves(int n) {
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            // Generate a list of valid moves for the current board
            List<Integer> validMoves = new ArrayList<>();
            for (int move = 0; move < 4; move++) {
                if (isValidMove(move)) {
                    validMoves.add(move);
                }
            }

            // Randomly select a move from the list of valid moves
            if (!validMoves.isEmpty()) {
                int randomMove = validMoves.get(random.nextInt(validMoves.size()));
                performMove(randomMove);
            }
        }
    }

    protected boolean isValidMove(int move) {
        // Check if the move is valid based on the current empty position
        switch (move) {
            case UP:
                return emptyRow > 0;
            case DOWN:
                return emptyRow < size - 1;
            case LEFT:
                return emptyCol > 0;
            case RIGHT:
                return emptyCol < size - 1;
            default:
                return false; // Invalid move
        }
    }

    public void performMove(int move) {
        switch (move) {
            case UP:
                if (emptyRow > 0) {
                    swap(emptyRow, emptyCol, emptyRow - 1, emptyCol);
                    emptyRow--;
                }
                break;
            case DOWN:
                if (emptyRow < size - 1) {
                    swap(emptyRow, emptyCol, emptyRow + 1, emptyCol);
                    emptyRow++;
                }
                break;
            case LEFT:
                if (emptyCol > 0) {
                    swap(emptyRow, emptyCol, emptyRow, emptyCol - 1);
                    emptyCol--;
                }
                break;
            case RIGHT:
                if (emptyCol < size - 1) {
                    swap(emptyRow, emptyCol, emptyRow, emptyCol + 1);
                    emptyCol++;
                }
                break;
            default:
                System.out.println("Invalid move. Please use 0 (Up), 1 (Down), 2 (Left), or 3 (Right).");
        }
    }

    private void swap(int row1, int col1, int row2, int col2) {
        int temp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = temp;
    }

    public List<Puzzle> generatePossibleMoves() {
        List<Puzzle> possibleMoves = new ArrayList<>();

        for (int move = 0; move < 4; move++) {
            if (isValidMove(move)) {
                Puzzle nextPuzzle = new Puzzle(this);
                nextPuzzle.performMove(move);
                possibleMoves.add(nextPuzzle);
            }
        }

        return possibleMoves;
    }

    public int[] getGoalCoordinates(int value) {
        int res = 0;
        if (value == 0) return new int[] {size-1,size-1};

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                res++;
                if (res == value) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String puzzleToString = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                puzzleToString += ((board[i][j] != 0 ? board[i][j] : "-") + "\t");
            }
            puzzleToString += "\n";
        }
        puzzleToString += "\n";
        return puzzleToString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
        result = prime * result + Arrays.deepHashCode(board);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Puzzle other = (Puzzle) obj;
        if (size != other.size)
            return false;
        if (!Arrays.deepEquals(board, other.board))
            return false;
        return true;
    }



}
