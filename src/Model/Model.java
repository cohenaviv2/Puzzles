package Model;

import Model.Graph.Heuristic.PuzzleHeuristic;
import Model.Puzzles.*;

public class Model {
    private PuzzleGraph puzzleGraph;
    private int size;

    public Model() {
        this.puzzleGraph = new PuzzleGraph();
    }

    public Puzzle createRandomPuzzle(int size, int randomMoves) {
        switch (size) {
            case 3:
                this.size = size;
                return new EightPuzzle(randomMoves);
            case 4:
                this.size = size;
                return new FifteenPuzzle(randomMoves);
            case 5:
                this.size = size;
                return new TwentyFourPuzzle(randomMoves);
            default:
                throw new IllegalArgumentException("Invalid size.");
        }
    }

    public Puzzle createCustomPuzzle(int size, int[][] startingBoard) throws IllegalArgumentException {
        try {
            switch (size) {
                case 3:
                    return new EightPuzzle(startingBoard);
                case 4:
                    return new FifteenPuzzle(startingBoard);
                case 5:
                    return new TwentyFourPuzzle(startingBoard);
                default:
                    throw new IllegalArgumentException("Invalid board.");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public Solution solve(Puzzle puzzle, String algorithm) throws InterruptedException {
         switch (algorithm) {
                case ALGORITHMS.BFS:
                    return puzzleGraph.breadthFirstSearch(puzzle);
                case ALGORITHMS.DIJKSTRA:
                    return puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Zero_Heuristic);
                case ALGORITHMS.ASTAR_MANHATTAN:
                    return puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Manhattan_Distance);
                case ALGORITHMS.ASTAR_EUCLIDEAN:
                    return puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Euclidean_Distance);
                case ALGORITHMS.ASTAR_MISPLACED:
                    return puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Misplaced_Tiles);
                default:
                    throw new IllegalArgumentException("Invalid algorithm.");
         }
    }

    public int estimateDifficulty(Puzzle puzzle) {
        // Calculate the estimate using Manhattan distance
        double estimate = PuzzleHeuristic.Manhattan_Distance.calculate(puzzle);

        if (estimate < 0 || estimate > (size == 5 ? 100 : 58)) {
            throw new IllegalArgumentException("Input value must be between 0 and 58 (inclusive). input: " + estimate);
        }

        double divider = size == 5 ? (90 / 4) : (58 / 4);
        double scaledValue = estimate / divider;
        int difficulty = (int) Math.floor(scaledValue + 0.5); // Add 0.5 to round to the nearest integer

        // Ensure the difficulty is in the range [1, 5]
        return Math.min(difficulty + 1, 5);
    }

    public class ALGORITHMS {
        public static final String BFS = "BFS";
        public static final String DIJKSTRA = "DIJKSTRA";
        public static final String ASTAR_MANHATTAN = "ASTAR_MANHATTAN";
        public static final String ASTAR_EUCLIDEAN = "ASTAR_EUCLIDEAN";
        public static final String ASTAR_MISPLACED = "ASTAR_MISPLACED";
    }

}
