package Graph.Heuristic;

import Puzzles.Puzzle;


public interface HeuristicFunction {
    double calculate(Puzzle puzzle);

    void printCache();
}