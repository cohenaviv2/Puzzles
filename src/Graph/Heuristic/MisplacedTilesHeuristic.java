package Graph.Heuristic;

import Puzzles.Puzzle;

public class MisplacedTilesHeuristic implements HeuristicFunction {
      @Override
    public double calculate(Puzzle puzzle) {
        int misplacedTiles = 0;
        int size = puzzle.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = puzzle.getBoard()[i][j];
                if (value != 0 && value != i * size + j + 1) {
                    // Check if the tile is not in its correct position
                    misplacedTiles++;
                }
            }
        }

        return misplacedTiles;
    }
}
