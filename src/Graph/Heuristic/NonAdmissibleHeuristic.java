package Graph.Heuristic;

import Puzzles.Puzzle;

public class NonAdmissibleHeuristic implements HeuristicFunction {
    @Override
    public double calculate(Puzzle puzzle) {
        // Return a constant value greater than or equal to the actual cost
        return 10.0;
    }

    @Override
    public void printCache() {
    }
    
}
