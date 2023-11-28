import Puzzles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Graph.Heuristic.PuzzleHeuristic;

public class Main {
    static List<Solution> solutionsList = new ArrayList<>();

    private static void printAverageSolution(TimeUnit timeUnit) {
        int avgNumOfVertices = 0, avgMomvements = 0;
        long avgElapsedTime = 0;
        for (Solution solution : solutionsList) {
            avgElapsedTime += solution.getElapsedTime();
            avgNumOfVertices += solution.getNumOfVertices();
            avgMomvements += solution.getNumOfMovements();
        }
        int size = solutionsList.size();
        long totalTime = avgElapsedTime;
        avgElapsedTime /= size;
        avgNumOfVertices /= size;
        avgMomvements /= size;

        StringBuilder sb = new StringBuilder();
        sb.append("Total time: " + Solution.getElapsedTime(timeUnit, totalTime) + "\n");
        sb.append("(Average) Number of vertices: " + Solution.getFormattedNumber(avgNumOfVertices) + "\n");
        sb.append("(Average) Time: " + Solution.getElapsedTime(timeUnit, avgElapsedTime) + " "
                + timeUnit.name().toLowerCase()
                + "\n");
        sb.append("(Average) Number of movements to solution: " + avgMomvements + "\n");
        System.out.println(sb.toString());
    }

    private static void runSolvingExperiment(Class<? extends Puzzle> puzzleType, int radomMoves, int rounds,
            boolean printEachSolution, TimeUnit timeUnit) {
        /*
         * This method creates a single Puzzle from the puzzle type given, with the
         * number of random moves given,
         * And solves this puzzle using all algorithms - BFS, AStar(Zero function),
         * AStar(Manhattan) and AStar(Nilsson sequence).
         * The method solves each puzzle as described for 'numRounds' times.
         * Finally prints the average solutions information with the TimeUnit given
         * format.
         */
        for (int i = 0; i < rounds; i++) {
            try {
                // Create Puzzle instance of the specified type
                Puzzle puzzle = puzzleType.getDeclaredConstructor(int.class).newInstance(radomMoves);

                System.out.println("\n\n##### ROUND " + (i + 1) + " #####\n\n");

                // Solve the puzzle using different algorithms
                List<Solution> solutions = PuzzleGraph.solveWithDifferentAlgorithms(puzzle, printEachSolution,timeUnit);

                // Process and add solutions
                for (Solution s : solutions) {
                    solutionsList.add(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {

        // runSolvingExperiment(FifteenPuzzle.class, 100, 1, true, TimeUnit.SECONDS);
        // Print average solution
        // printAverageSolution(TimeUnit.SECONDS);

        // Test 1
        PuzzleGraph pg = new PuzzleGraph();

        Puzzle p1 = new FifteenPuzzle(100);
        System.out.println(p1);

        System.out.println("Manhattan_Distance");
        Solution s1 = pg.AStarSearch(p1, PuzzleHeuristic.Manhattan_Distance);
        s1.print(TimeUnit.SECONDS);
        // PuzzleHeuristic.Manhattan_Distance.printCache();
        
        System.out.println("Permutations_Inversions");
        Solution s2 = pg.AStarSearch(p1, PuzzleHeuristic.Permutations_Inversions);
        s2.print(TimeUnit.SECONDS);
        // PuzzleHeuristic.Permutations_Inversions.printCache();

        s1.printMovesToSolution();
        s2.printMovesToSolution();

    }
}
