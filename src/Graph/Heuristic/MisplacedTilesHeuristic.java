package Graph.Heuristic;

import java.util.HashMap;
import java.util.Map;

import Puzzles.Puzzle;

public class MisplacedTilesHeuristic implements HeuristicFunction {
    private Map<Puzzle,Integer> heuristicCache = new HashMap<>();

      @Override
    public double calculate(Puzzle puzzle) {
          // Check if the heuristic value is already in the cache
        if (heuristicCache.containsKey(puzzle)) {
            return heuristicCache.get(puzzle);
        }
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

        // Cache the computed heuristic value
        heuristicCache.put(puzzle, misplacedTiles);
        return misplacedTiles;
    }

    @Override
    public void printCache() {
        heuristicCache.forEach((k,v)->System.out.println(v));
    }
}
