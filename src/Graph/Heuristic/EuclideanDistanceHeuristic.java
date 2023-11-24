package Graph.Heuristic;

import Puzzles.Puzzle;

public class EuclideanDistanceHeuristic implements HeuristicFunction {
    @Override
    public double calculate(Puzzle puzzle) {
        int distance = 0;
        int size = puzzle.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = puzzle.getBoard()[i][j];

                if (value != 0) {
                    int[] goalCoordinates = puzzle.getCoordinates(value);
                    distance += Math.pow(i - goalCoordinates[0], 2) + Math.pow(j - goalCoordinates[1], 2);
                }
            }
        }

        return Math.sqrt(distance);
    }
}

