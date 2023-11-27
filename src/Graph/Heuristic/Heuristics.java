package Graph.Heuristic;

public class Heuristics {
    public static ZeroHeuristic Zero_Heuristic = ZeroHeuristic.get();
    public static ManhattanDistanceHeuristic Manhattan_Distance = ManhattanDistanceHeuristic.get();
    public static EuclideanDistanceHeuristic Euclidean_Distance = EuclideanDistanceHeuristic.get();
    public static MisplacedTilesHeuristic Misplaced_Tiles = MisplacedTilesHeuristic.get();
    public static NilssonSequenceHeuristic Nilsson_Sequence = NilssonSequenceHeuristic.get();
}
