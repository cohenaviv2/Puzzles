package Model.Graph.Heuristic;

import Model.Puzzles.Puzzle;

public interface PuzzleHeuristic {
    double calculate(Puzzle puzzle);
    void printCache();

    // All heuristic functions
    public static ZeroHeuristic Zero_Heuristic = ZeroHeuristic.get();
    public static ManhattanDistanceHeuristic Manhattan_Distance = ManhattanDistanceHeuristic.get();
    public static EuclideanDistanceHeuristic Euclidean_Distance = EuclideanDistanceHeuristic.get();
    public static MisplacedTilesHeuristic Misplaced_Tiles = MisplacedTilesHeuristic.get();
    public static InversionsHeuristic Permutations_Inversions = InversionsHeuristic.get();
}
