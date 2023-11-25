import Graph.Heuristic.*;
import Puzzles.*;

public class Main {
    public static void solveWithDifferentAlgorithms(Puzzle puzzle, int rounds) {
        // print the puzzle
        System.out.println(puzzle);
        // Solve with all search algorithms (BFS and AStar with 3 diffrenet heuristic)
        for (int i = 0; i < rounds; i++) {
            PuzzleGraph.solveWithDifferentAlgorithms(puzzle);
        }
    }

    public static void main(String[] args) {
        int n = 50;

        // Create 15-Puzzle with n random moves from the solution borad
        Puzzle fifteenPuzzle = new FifteenPuzzle(n);

        // Create 24-Puzzle with n random moves from the solution borad
        Puzzle twentyFourPuzzle = new TwentyFourPuzzle(n);

        int round = 1;
        solveWithDifferentAlgorithms(twentyFourPuzzle, round);

    }
}
