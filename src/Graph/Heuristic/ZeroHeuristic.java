package Graph.Heuristic;

import Puzzles.Puzzle;

public class ZeroHeuristic implements HeuristicFunction {
        @Override
    public int calculate(Puzzle puzzle) {
        // Zero heuristic (always returns 0)
        return 0;
    }
}
