package Graph.Heuristic;

import Puzzles.Puzzle;

@FunctionalInterface
public interface HeuristicFunction {
    double calculate(Puzzle puzzle);
}