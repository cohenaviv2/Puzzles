import Puzzles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Graph.Heuristic.PuzzleHeuristic;

public class Main {

    private static void printAverageSolution(List<Solution> solutions, TimeUnit timeUnit) {
        int avgNumOfVertices = 0, avgMomvements = 0;
        long avgElapsedTime = 0;
        for (Solution solution : solutions) {
            avgElapsedTime += solution.getElapsedTime();
            avgNumOfVertices += solution.getNumOfVertices();
            avgMomvements += solution.getNumOfMovements();
        }
        int size = solutions.size();
        long totalTime = avgElapsedTime;
        avgElapsedTime /= size;
        avgNumOfVertices /= size;
        avgMomvements /= size;

        StringBuilder sb = new StringBuilder();
        sb.append("Total time: " + Solution.getElapsedTime(timeUnit, totalTime) + "\n");
        sb.append("Average Number of vertices: " + Solution.getFormattedNumber(avgNumOfVertices) + "\n");
        sb.append("Average Time: " + Solution.getElapsedTime(timeUnit, avgElapsedTime) + " "
                + timeUnit.name().toLowerCase()
                + "\n");
        sb.append("Average Number of movements to solution: " + avgMomvements + "\n");
        System.out.println(sb.toString());
    }

    private static void runSolvingExperiment(Class<? extends Puzzle> puzzleType, int radomMoves, int numOfPuzzles,
            boolean printEachSolution, TimeUnit timeUnit) {
        /*
         * This method creates a list of random puzzles from the puzzle type given,
         * And solves this puzzles using all algorithms - BFS, AStar(Zero function),
         * AStar(Manhattan) and AStar(Nilsson sequence).
         * Finally prints the average solution for each algorithm with the TimeUnit
         * format.
         */
        PuzzleGraph puzzleGraph = new PuzzleGraph();
        List<Puzzle> randomPuzzles = new ArrayList<>();
        try {
            // Create random puzzles
            for (int i = 0; i < numOfPuzzles; i++) {
                Puzzle puzzle = puzzleType.getDeclaredConstructor(int.class).newInstance(radomMoves);
                randomPuzzles.add(puzzle);
            }

            // Solve puzzles using each algorithm
            List<Solution> solutions = new ArrayList<>();
            System.out.println("------------------------------------- BFS -------------------------------------");
            for (int i = 0; i < numOfPuzzles; i++) {
                // Solve the puzzle using BFS
                solutions.add(puzzleGraph.breadthFirstSearch(randomPuzzles.get(i)));
            }
            // Print solutions
            printAverageSolution(solutions, timeUnit);
            solutions.clear();

            System.out.println("----------------------------- AStar (Zero func) ------------------------------");
            for (int i = 0; i < numOfPuzzles; i++) {
                // Solve the puzzle using A* with zero heuristic
                solutions.add(puzzleGraph.AStarSearch(randomPuzzles.get(i), PuzzleHeuristic.Zero_Heuristic));
            }
            // Print solutions
            printAverageSolution(solutions, timeUnit);
            solutions.clear();

            System.out.println("-----------------------------  AStar (Manhattan)  -----------------------------");
            for (int i = 0; i < numOfPuzzles; i++) {
                // Solve the puzzle using A* with Manhattan heuristic
                solutions.add(puzzleGraph.AStarSearch(randomPuzzles.get(i), PuzzleHeuristic.Manhattan_Distance));
            }
            // Print solutions
            printAverageSolution(solutions, timeUnit);
            solutions.clear();

            System.out.println("-------------------------- AStar (Permutation inversions) --------------------------");
            for (int i = 0; i < numOfPuzzles; i++) {
                // Solve the puzzle using A* with Permutation inversions heuristic
                solutions.add(puzzleGraph.AStarSearch(randomPuzzles.get(i), PuzzleHeuristic.Permutations_Inversions));
            }
            // Print solutions
            printAverageSolution(solutions, timeUnit);
            solutions.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        int radomMoves = 10;
        int numOfPuzzles = 50;
        int times = 5;

        // Test 1 - solve random 15-puzzle with all algorithm x5
        System.out.println("*******************\tTEST 1\t*******************");
        for (int i = 0; i < times; i++) {
            // Create random puzzle
            Puzzle puzzle15 = new FifteenPuzzle(radomMoves);
            System.out.println(puzzle15);
            // Solve using all algorithms & print solutions
            PuzzleGraph.solveWithDifferentAlgorithms(puzzle15, true, timeUnit);
        }

        // Test 2 - solve random 24-puzzle with all algorithm x5
        System.out.println("*******************\tTEST 2\t*******************");
        for (int i = 0; i < times; i++) {
            // Create random puzzle
            Puzzle puzzle15 = new TwentyFourPuzzle(radomMoves);
            System.out.println(puzzle15);
            // Solve using all algorithms & print solutions
            PuzzleGraph.solveWithDifferentAlgorithms(puzzle15, true, timeUnit);
        }

        // Test 3 - solve 50 random 15-puzzles with each algorithm
        System.out.println("*******************\tTEST 3\t*******************");
        runSolvingExperiment(FifteenPuzzle.class, radomMoves, numOfPuzzles, false, timeUnit);

        // Test 4 - solve 50 random 24-puzzles with each algorithm
        System.out.println("*******************\tTEST 4\t*******************");
        runSolvingExperiment(TwentyFourPuzzle.class, radomMoves, numOfPuzzles, false, timeUnit);
    }
}
