package Graph.Heuristic;

import Puzzles.Puzzle;

@FunctionalInterface
public interface HeuristicFunction {
    int calculate(Puzzle puzzle);
}