package Graph.Heuristic;

import Puzzles.Puzzle;

public class ManhattanDistanceHeuristic implements HeuristicFunction {
        @Override
    public double calculate(Puzzle puzzle) {
        int distance = 0;
        int size = puzzle.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = puzzle.getBoard()[i][j];

                if (value != 0) {
                    int[] goalCoordinates = puzzle.getCoordinates(value);
                    distance += Math.abs(i - goalCoordinates[0]) + Math.abs(j - goalCoordinates[1]);
                }
            }
        }

        return distance;
    }
}
