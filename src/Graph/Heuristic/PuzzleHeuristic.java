package Graph.Heuristic;

import Puzzles.Puzzle;

public interface PuzzleHeuristic {
    double calculate(Puzzle puzzle);
    void printCache();
}