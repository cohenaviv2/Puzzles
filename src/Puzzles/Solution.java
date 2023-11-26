package Puzzles;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.*;

import Graph.Graph;

public class Solution {
    private final Graph graph;
    private final int numOfVertices;
    private final int developedVertices;
    private final long elapsedTime;
    private final String memoryUsage;
    private final int solutionVertexId;
    private final List<Integer> path;
    private final int numOfMovements;
    private final List<Puzzle> movementsList;

    public Solution(Graph graph, long startTime, int developedVertices, int solutionVertexId,
            Map<Integer, Puzzle> states) {
        this.graph = graph;
        this.numOfVertices = this.graph.size();
        this.developedVertices = developedVertices;
        this.solutionVertexId = solutionVertexId;
        // Elapsed Time
        long endTime = System.nanoTime();
        this.elapsedTime = endTime - startTime;
        // Heap Memory Usage (MB)
        Runtime runtime = Runtime.getRuntime();
        double memoryUsedInBytes = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsedInMegabytes = memoryUsedInBytes / (1024 * 1024);
        this.memoryUsage = String.format("%.2f", memoryUsedInMegabytes);
        // Path to Solution
        this.path = new ArrayList<>();
        int currentId = solutionVertexId;
        while (currentId != -1) {
            path.add(currentId);
            currentId = this.graph.getVertex(currentId).getPi() != null ? this.graph.getVertex(currentId).getPi().ID : -1;
        }
        this.numOfMovements = path.size() - 1;
        // Movements to Solution
        this.movementsList = new ArrayList<>();
        int totalSteps = path.size();
        for (int i = totalSteps - 1; i >= 0; i--) {
            int vertexId = path.get(i);
            this.movementsList.add(states.get(vertexId));
        }
    }

    public void print(TimeUnit timeUnit) {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices in the Graph: " + getFormattedNumber(numOfVertices) + "\n");
        sb.append("Developed vertices: " + getFormattedNumber(developedVertices) + "\n");
        sb.append("Time: " + getElapsedTime(timeUnit,elapsedTime) + " " + timeUnit.name().toLowerCase() + "\n");
        sb.append("Heap Memory Usage: " + memoryUsage + " MB\n");
        sb.append("Soultion vertex ID: " + solutionVertexId + "\n");
        // Print the path in reverse order
        sb.append("Solution path: ");
        for (int i = path.size() - 1; i >= 0; i--) {
            int vertexId = path.get(i);
            sb.append(vertexId);
            if (i > 0) {
                sb.append("->");
            }
        }
        sb.append("\n");
        sb.append("Number of movements in the solution path: " + numOfMovements + "\n");
        System.out.println(sb.toString());
    }

    public void printMovesToSolution() {
        System.out.println("Moves to Solution:\n");
        Timer timer = new Timer();
        int totalSteps = path.size();

        for (int i = 0; i < totalSteps; i++) {
            final int stepIndex = i;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("\nStep " + (stepIndex) + ":\n" + movementsList.get(stepIndex));

                    if (stepIndex == 0) {
                        timer.cancel();
                    }
                }
            }, (totalSteps - stepIndex) * 1000);
        }
    }

    private static String getFormattedNumber(int number) {
        // Create a DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(number);
        return formattedNumber;
    }

    private static String getElapsedTime(TimeUnit timeUnit, long elapsedTime) {
        switch (timeUnit) {
            case SECONDS:
                double secondsWithDot = elapsedTime / 1e9;
                return String.format("%.4f", secondsWithDot);
            case MILLISECONDS:
                return String.valueOf(elapsedTime / 1_000_000);
            case MICROSECONDS:
                return String.valueOf(elapsedTime / 1_000);
            case NANOSECONDS:
                return String.valueOf(elapsedTime);
            default:
                throw new IllegalArgumentException("Invalid time unit: " + timeUnit);
        }
    }

    public Graph toGraph() {
        return graph;
    }

    public int getNumOfVertices() {
        return numOfVertices;
    }

    public int getDevelopedVertices() {
        return developedVertices;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public int getSolutionVertexId() {
        return solutionVertexId;
    }

    public int getNumOfMovements() {
        return numOfMovements;
    }

}
