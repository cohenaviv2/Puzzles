package Graph.Heuristic;

import java.util.*;

import Puzzles.Puzzle;

public class NilssonSequenceHeuristic implements PuzzleHeuristic {
    private Map<Puzzle, Integer> heuristicCache = new HashMap<>();
    private static NilssonSequenceHeuristic instance = null;

    private NilssonSequenceHeuristic(){
        this.heuristicCache = new HashMap<>();
    }

    public static NilssonSequenceHeuristic get() {
        if (instance == null)
            instance = new NilssonSequenceHeuristic();
        return instance;
    }

    @Override
    public double calculate(Puzzle puzzle) {
        // Check if the heuristic value is already in the cache
        if (heuristicCache.containsKey(puzzle)) {
            return heuristicCache.get(puzzle);
        }

        int[][] board = puzzle.getBoard();
        int size = puzzle.getSize();
        int cost = 0;

        // Locate the empty tile
        int emptyRow = -1;
        int emptyCol = -1;
        outerLoop: for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break outerLoop;
                }
            }
        }

        // Apply Nilsson's sequence
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int targetValue = i * size + j + 1;

                // Check if targetValue is within the valid range
                if (targetValue <= size * size) {
                    int[] targetCoordinates = puzzle.getGoalCoordinates(targetValue);

                    while (emptyRow != targetCoordinates[0] || emptyCol != targetCoordinates[1]) {
                        if (emptyRow < targetCoordinates[0]) {
                            // Move empty tile down
                            cost += 1;
                            emptyRow++;
                        } else if (emptyRow > targetCoordinates[0]) {
                            // Move empty tile up
                            cost += 1;
                            emptyRow--;
                        }

                        if (emptyCol < targetCoordinates[1]) {
                            // Move empty tile right
                            cost += 1;
                            emptyCol++;
                        } else if (emptyCol > targetCoordinates[1]) {
                            // Move empty tile left
                            cost += 1;
                            emptyCol--;
                        }
                    }
                }
            }
        }

        heuristicCache.put(puzzle, cost);
        return cost;
    }

    @Override
    public void printCache() {
        // Print cached sequences
        heuristicCache.forEach((k, v) -> {
            System.out.println("Puzzle:\n" + k);
            System.out.println("Nilsson Sequence: " + v + "\n");
        });
    }
}
