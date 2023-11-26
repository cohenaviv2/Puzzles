import Puzzles.*;

import java.util.concurrent.TimeUnit;

import Graph.Heuristic.*;

public class Main {

    public static void main(String[] args) {
        PuzzleGraph puzzleGraph = new PuzzleGraph();
        int n = 50;
        int times = 1;

        // for (int i = 0; i < times; i++) {
        //     System.out.println("\n\n\n######\tROUND " + (i + 1) + "\t######\n\n\n");

        //     // Create 15-Puzzle with n random moves from the solution borad
        //     Puzzle fifteenPuzzle = new FifteenPuzzle(n);
        //     System.out.println(fifteenPuzzle);
        //     PuzzleGraph.solveWithDifferentAlgorithms(fifteenPuzzle,true,TimeUnit.SECONDS);
        // }

        Puzzle puzzle_24 = new TwentyFourPuzzle(n);
        Solution solution1 = puzzleGraph.AStarSearch(puzzle_24, new NonAdmissibleHeuristic());
        solution1.print(TimeUnit.SECONDS);
        Solution solution2 = puzzleGraph.AStarSearch(puzzle_24, new ManhattanDistanceHeuristic());
        solution2.print(TimeUnit.SECONDS);

    }
}
