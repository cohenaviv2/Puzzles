package Graph.Heuristic;

import java.util.HashMap;
import java.util.Map;

import Puzzles.Puzzle;

public class EuclideanDistanceHeuristic implements HeuristicFunction {
    private Map<Puzzle,Double> heuristicCache = new HashMap<>();

    @Override
    public double calculate(Puzzle puzzle) {
        if (heuristicCache.containsKey(puzzle)) {
            return heuristicCache.get(puzzle);
        }
        double distance = 0;
        int size = puzzle.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = puzzle.getBoard()[i][j];

                if (value != 0) {
                    int[] goalCoordinates = puzzle.getGoalCoordinates(value);
                    distance += Math.pow(i - goalCoordinates[0], 2) + Math.pow(j - goalCoordinates[1], 2);
                }
            }
        }

        // Cache the computed heuristic value
        heuristicCache.put(puzzle, distance);
        return Math.sqrt(distance);
    }

    // @Override
    // public double calculate(Puzzle puzzle) {
    //     // Check if the heuristic value is already in the cache
    //     if (heuristicCache.containsKey(puzzle)) {
    //         return heuristicCache.get(puzzle);
    //     }

    //     int size = puzzle.getSize();
    //     double distance = 0;

    //     for (int i = 0; i < size; i++) {
    //         for (int j = 0; j < size; j++) {
    //             int value = puzzle.getBoard()[i][j];

    //             if (value != 0) {
    //                 int[] goalCoordinates = puzzle.getGoalCoordinates(value);
    //                 double dx = i - goalCoordinates[0];
    //                 double dy = j - goalCoordinates[1];
    //                 distance += Math.sqrt(dx * dx + dy * dy);
    //             }
    //         }
    //     }

    //     // Cache the computed heuristic value
    //     heuristicCache.put(puzzle, distance);
    //     return distance;
    // }

    @Override
    public void printCache() {
        heuristicCache.forEach((k,v)->System.out.println(v));
    }
}

