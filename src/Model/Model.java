package Model;

import java.util.*;
import java.util.concurrent.TimeUnit;

import Model.Graph.Heuristic.PuzzleHeuristic;
import Model.Puzzles.*;

public class Model extends Observable {
    private PuzzleGraph puzzleGraph;
    private List<Solution> solutions;
    private int size;

    public Model() {
        puzzleGraph = new PuzzleGraph();
        solutions = new ArrayList<>();
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

    public void Solve(Puzzle puzzle, List<String> algorithmList) {
        for (String algorithm : algorithmList) {
            switch (algorithm) {
                case ALGORITHMS.BFS:
                System.out.println("model BFS");
                    solutions.add(puzzleGraph.breadthFirstSearch(puzzle));
                    System.out.println("BFS done: "+solutions.get(0).toString(TimeUnit.SECONDS));
                    setChanged();
                    notifyObservers(ALGORITHMS.BFS + " solved!");
                    break;
                case ALGORITHMS.DIJKSTRA:
                    solutions.add(puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Zero_Heuristic));
                    setChanged();
                    notifyObservers(ALGORITHMS.DIJKSTRA + " solved!");
                    break;
                case ALGORITHMS.ASTAR_MANHATTAN:
                    solutions.add(puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Manhattan_Distance));
                    setChanged();
                    notifyObservers(ALGORITHMS.ASTAR_MANHATTAN + " solved!");
                    break;
                case ALGORITHMS.ASTAR_EUCLIDEAN:
                    solutions.add(puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Euclidean_Distance));
                    setChanged();
                    notifyObservers(ALGORITHMS.ASTAR_EUCLIDEAN + " solved!");
                    break;
                case ALGORITHMS.ASTAR_MISPLACED:
                    solutions.add(puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Misplaced_Tiles));
                    setChanged();
                    notifyObservers(ALGORITHMS.ASTAR_MISPLACED + " solved!");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid algorithm.");
            }
        }
    }

    public List<Solution> geSolutions() {
        return solutions;
    }

    public int estimateDifficulty(Puzzle puzzle) {
        // Calculate the estimate using Manhattan distance
        double estimate = PuzzleHeuristic.Manhattan_Distance.calculate(puzzle);

        if (estimate < 0 || estimate > (size == 5 ? 100 : size == 4 ? 58 : 30)) {
            throw new IllegalArgumentException("Input value must be between 0 and 58 (inclusive). input: " + estimate);
        }

        double divider = size == 5 ? (100/4) : size == 4 ? (58/4) : (30/4);
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
