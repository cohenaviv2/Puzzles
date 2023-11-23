package Puzzles;

import Graph.*;
import java.util.*;
import java.text.DecimalFormat;

public class PuzzleGraph {
    private Graph puzzleGraph;
    private Puzzle initialPuzzle;
    private Map<Integer, Puzzle> puzzleStates; // ID to Puzzle
    private int solutionId; // Store the ID of the solved puzzle vertex
    private boolean solved;
    private long startTime;

    public PuzzleGraph(Puzzle initialPuzzle) {
        this.puzzleGraph = new Graph();
        this.initialPuzzle = initialPuzzle;
        this.puzzleStates = new HashMap<>();
    }

    public void search_BFS() {
        if (!solved) {
            startTime = System.currentTimeMillis();
            if (initialPuzzle.isSolved()) {
                printSolution();
                return;
            }
            LinkedList<Integer> queue = new LinkedList<>();
            Set<Puzzle> visited = new HashSet<>();

            int startId = puzzleGraph.addVertex();
            puzzleGraph.getVertex(startId).setPi(null);
            puzzleStates.put(startId, initialPuzzle);
            queue.offer(startId);
            visited.add(initialPuzzle);

            while (!queue.isEmpty()) {
                int currentId = queue.poll();
                Puzzle currentPuzzle = puzzleStates.get(currentId);

                if (currentPuzzle.isSolved()) {
                    solutionId = currentId;
                    printSolution();
                    return;
                }

                List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();

                for (Puzzle nextPuzzle : nextStates) {
                    if (!visited.contains(nextPuzzle)) {
                        int nextId = puzzleGraph.addVertex();
                        puzzleGraph.getVertex(nextId).setPi(puzzleGraph.getVertex(currentId));
                        puzzleStates.put(nextId, nextPuzzle);
                        puzzleGraph.addUndirectedEdge(currentId, nextId);
                        if (nextPuzzle.isSolved()) {
                            queue.addFirst(nextId);
                        } else {
                            queue.offer(nextId);
                        }
                        visited.add(nextPuzzle);
                    }
                }
            }
        }
    }

    public void search_AStar() {
        if (!solved) {
            if (initialPuzzle.isSolved())
                return;

        }
    }

    private void printSolution() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nSolved!\n\n");
        sb.append("Num of vertices: " + getNumOfVertices() + "\n");
        sb.append("Time: " + elapsedTime() + " seconds\n");
        sb.append("Memory Used: " + memoryUsage() + " MB\n");
        sb.append("Soultion vertex ID: " + solutionId + "\n");
        sb.append("Solution path:");
        Vertex v = puzzleGraph.getVertex(solutionId);
        int cnt= 0;
        while (v.getPi() != null) {
            sb.append(v.ID);
            if (v.getPi().getPi() != null) {
                cnt++;
                sb.append("->");
            } else {
                sb.append("\n");
            }
            v = v.getPi();
        }
        sb.append("Num of vertices in the solution path: "+cnt+"\n");
        System.out.println(sb.toString());
    }

    private String memoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        double memoryUsedInBytes = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsedInMegabytes = memoryUsedInBytes / (1024 * 1024);
    
        // Format the result to have a dot and three digits
        return String.format("%.3f", memoryUsedInMegabytes);
    }
    
    private String elapsedTime() {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        // Convert milliseconds to seconds with dot and milliseconds
        double secondsWithDot = elapsedTime / 1000.0;
        return String.format("%.3f", secondsWithDot);
    }

    public Graph toGraph() {
        return puzzleGraph;
    }

    private String getNumOfVertices() {
        // Create a DecimalFormat object with the pattern "#,###"
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // Format the number using the DecimalFormat
        String formattedNumber = decimalFormat.format(puzzleGraph.size());
        return formattedNumber;
    }

    public int getSolutionId() {
        return solutionId;
    }

    @Override
    public String toString() {
        return puzzleGraph.toString();
    }

    public static void main(String[] args) {

        // Create 15-Puzzle with 50 random moves from the solution borad
        Puzzle puzzle15 = new FifteenPuzzle(10);
        System.out.println(puzzle15);

       // Solve puzzle using BFS
        PuzzleGraph puzzle15Graph = new PuzzleGraph(puzzle15);
        puzzle15Graph.search_BFS();

        // // Create 24-Puzzle with 50 random moves from the solution borad
        // Puzzle puzzle24 = new TwentyFourPuzzle(50);
        // System.out.println(puzzle24);

        // // Solve puzzle using BFS
        // PuzzleGraph puzzle24Graph = new PuzzleGraph(puzzle24);
        // puzzle24Graph.search_BFS();

    }
}
