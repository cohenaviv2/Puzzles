import Puzzles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    static List<Solution> solutionsList = new ArrayList<>();

    private static void printAverageSolution(TimeUnit timeUnit) {
        int avgNumOfVertices = 0, avgDevelopedVertices = 0, avgMomvements = 0;
        long avgElapsedTime = 0;
        for (Solution solution : solutionsList) {
            avgElapsedTime += solution.getElapsedTime();
            avgNumOfVertices += solution.getNumOfVertices();
            avgDevelopedVertices += solution.getDevelopedVertices();
            avgMomvements += solution.getNumOfMovements();
        }
        int size = solutionsList.size();
        long totalTime = avgElapsedTime;
        avgElapsedTime /= size;
        avgNumOfVertices /= size;
        avgDevelopedVertices /= size;
        avgMomvements /= size;

        StringBuilder sb = new StringBuilder();
        sb.append("Total time: " + Solution.getElapsedTime(timeUnit, totalTime) + "\n");
        sb.append("(Average) Number of vertices: " + Solution.getFormattedNumber(avgNumOfVertices) + "\n");
        sb.append("(Average) Developed vertices: " + Solution.getFormattedNumber(avgDevelopedVertices) + "\n");
        sb.append("(Average) Time: " + Solution.getElapsedTime(timeUnit, avgElapsedTime) + " "
                + timeUnit.name().toLowerCase()
                + "\n");
        sb.append("(Average) Number of movements to solution: " + avgMomvements + "\n");
        System.out.println(sb.toString());
    }

    private static void runSolvingExperiment(Class<? extends Puzzle> puzzleType, int numRounds, int numRandomMoves,
            boolean printEachSolution, TimeUnit timeUnit) {
        for (int i = 0; i < numRounds; i++) {
            try {
                // Create Puzzle instance of the specified type
                Puzzle puzzle = puzzleType.getDeclaredConstructor(int.class).newInstance(numRandomMoves);

                System.out.println("\n\n\n######\tROUND " + (i + 1) + "\t######\n\n\n");

                // Solve the puzzle using different algorithms
                List<Solution> solutions = PuzzleGraph.solveWithDifferentAlgorithms(puzzle, printEachSolution,
                        timeUnit);

                // Process and add solutions
                for (Solution s : solutions) {
                    solutionsList.add(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Print average solution if needed
        printAverageSolution(timeUnit);
    }

    public static void main(String[] args) {

        runSolvingExperiment(TwentyFourPuzzle.class, 50, 10, false, TimeUnit.SECONDS);
    }
}
