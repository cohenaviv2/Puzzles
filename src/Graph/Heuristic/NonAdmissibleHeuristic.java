package Graph.Heuristic;

import java.util.HashMap;
import java.util.Map;

import Puzzles.Puzzle;

public class NonAdmissibleHeuristic implements HeuristicFunction {
    private Map<Puzzle, Double> heuristicCache = new HashMap<>();

    @Override
    public double calculate(Puzzle puzzle) {
         // Check if the heuristic value is already in the cache
        if (heuristicCache.containsKey(puzzle)) {
            return heuristicCache.get(puzzle);
        }

        // Calculate the Manhattan Distance as usual
        HeuristicFunction manhattanDist = new ManhattanDistanceHeuristic();
        double distance = manhattanDist.calculate(puzzle);

        // Introduce randomness to overestimate the cost
        double overestimateFactor = 1.5; // You can adjust this factor
        double overestimatedDistance = distance * overestimateFactor;

        // Cache the computed heuristic value
        heuristicCache.put(puzzle, overestimatedDistance);
        return overestimatedDistance;
    }

    @Override
    public void printCache() {
        heuristicCache.forEach((k,v)->System.out.println(v));
    }
    
}
