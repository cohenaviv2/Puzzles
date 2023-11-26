import Puzzles.*;

import java.util.concurrent.TimeUnit;

import Graph.Heuristic.*;

public class Main {

    public static void main(String[] args) {
        int n = 10;
        int times = 1;

        for (int i = 0; i < times; i++) {
            System.out.println("\n\n\n######\tROUND " + (i + 1) + "\t######\n\n\n");

            // Create 15-Puzzle with n random moves from the solution borad
            Puzzle fifteenPuzzle = new FifteenPuzzle(n);
            System.out.println(fifteenPuzzle);
            PuzzleGraph.solveWithDifferentAlgorithms(fifteenPuzzle,true,TimeUnit.SECONDS);
        }


    }
}
