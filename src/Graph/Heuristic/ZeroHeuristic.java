package Graph.Heuristic;

import Puzzles.Puzzle;

public class ZeroHeuristic implements HeuristicFunction {
        @Override
    public double calculate(Puzzle puzzle) {
        // Zero heuristic (always returns 0)
        return 0;
    }
    
    @Override
    public void printCache() {
    }
}
