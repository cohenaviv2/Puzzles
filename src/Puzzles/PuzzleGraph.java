package Puzzles;

import Graph.*;
import Graph.Heuristic.*;

import java.util.*;
import java.text.DecimalFormat;

public class PuzzleGraph {
    private Graph graph;
    private Puzzle initialPuzzle;
    private Map<Integer, Puzzle> states; // ID to Puzzle
    private int solutionId; // Store the ID of the solved puzzle vertex
    private boolean isSolved;
    private long startTime;

    public PuzzleGraph(Puzzle initialPuzzle) {
        this.graph = new Graph();
        this.initialPuzzle = initialPuzzle;
        this.states = new HashMap<>();
    }

    public void breadthFirstSearch() {
        if (!isSolved) {
            // Set start time,queue and vistied set
            startTime = System.currentTimeMillis();
            Queue<Integer> queue = new LinkedList<>(); // Queue of vertex id
            Set<Puzzle> visited = new HashSet<>();

            // Create starting vertex
            int startId = graph.addVertex();
            Vertex startVertex = graph.getVertex(startId);
            states.put(startId, initialPuzzle);

            // Set pi,visited & Add to the queue
            startVertex.setPi(null);
            queue.offer(startId);
            visited.add(initialPuzzle);

            while (!queue.isEmpty()) {
                int currentId = queue.poll();
                Puzzle currentPuzzle = states.get(currentId);

                // Algorithm requires a stopping condition (solution space is exponential)
                if (currentPuzzle.isSolved()) {
                    solutionId = currentId;
                    printSolution(visited.size());
                    return;
                }

                // Generate possible board states
                List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();
                for (Puzzle nextPuzzle : nextStates) {
                    int nextId = graph.addVertex();
                    graph.addUndirectedEdge(currentId, nextId);
                    if (!visited.contains(nextPuzzle)) {
                        // If state not discovered yet - Set pi, visited & Add to the queue
                        Vertex nextVertex = graph.getVertex(nextId);
                        nextVertex.setPi(graph.getVertex(currentId));
                        states.put(nextId, nextPuzzle);
                        queue.offer(nextId);
                        visited.add(nextPuzzle);
                    }
                }
            }
        }
    }

    public void AStarSearch(HeuristicFunction heuristic) {
        if (!isSolved) {
            // Set start time
            startTime = System.currentTimeMillis();

            PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(
                    Comparator.comparingDouble(id -> graph.getVertex(id).getFunction()));
            Set<Puzzle> visited = new HashSet<>();

            // Create & Add the starting vertex and set pi,g,h
            int startId = graph.addVertex();
            Vertex startVertex = graph.getVertex(startId);
            startVertex.setPi(null);
            startVertex.setG(0);
            startVertex.setH(heuristic.calculate(initialPuzzle));
            states.put(startId, initialPuzzle);
            priorityQueue.offer(startId);
            visited.add(initialPuzzle);

            while (!priorityQueue.isEmpty()) {
                int currentId = priorityQueue.poll();
                // updateQueue(priorityQueue);
                Vertex currentVertex = graph.getVertex(currentId);
                Puzzle currentPuzzle = states.get(currentId);

                if (currentPuzzle.isSolved()) {
                    solutionId = currentId;
                    printSolution(visited.size());
                    return;
                }

                List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();

                for (Puzzle nextPuzzle : nextStates) {
                    int nextId = graph.addVertex();
                    Vertex nextVertex = graph.getVertex(nextId);
                    nextVertex.setPi(currentVertex);
                    int tentativeG = currentVertex.getG() + 1; // Assuming all edges have weight 1

                    if (tentativeG < nextVertex.getG() || !visited.contains(nextPuzzle)) {
                        nextVertex.setG(tentativeG);
                        nextVertex.setH(heuristic.calculate(nextPuzzle));
                        states.put(nextId, nextPuzzle);
                        graph.addUndirectedEdge(currentVertex.ID, nextId);
                        priorityQueue.offer(nextId);
                        visited.add(nextPuzzle);
                    }
                }
            }
        }
    }

    private void printSolution(int vis) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nSolved!\n\n");
        sb.append("Number of vertices: " + getNumOfVertices() + "\n");
        //
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // Format the number using the DecimalFormat
        String formattedNumber = decimalFormat.format(vis);
        sb.append("visited: " + formattedNumber + "\n");
        //
        sb.append("Time: " + elapsedTime() + " seconds\n");
        sb.append("Heap Memory Usage: " + memoryUsage() + " MB\n");
        sb.append("Soultion vertex ID: " + solutionId + "\n");
        sb.append("Solution path:");
        Vertex v = graph.getVertex(solutionId);
        int cnt = 0;
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
        sb.append("Number of movements in the solution path: " + cnt + "\n");
        System.out.println(sb.toString());
    }

    private String memoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        double memoryUsedInBytes = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsedInMegabytes = memoryUsedInBytes / (1024 * 1024);

        // Format the result to have a dot and three digits
        return String.format("%.2f", memoryUsedInMegabytes);
    }

    private String elapsedTime() {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        // Convert milliseconds to seconds with dot and milliseconds
        double secondsWithDot = elapsedTime / 1000.0;
        return String.format("%.3f", secondsWithDot);
    }

    public Graph toGraph() {
        return graph;
    }

    private String getNumOfVertices() {
        // Create a DecimalFormat object with the pattern "#,###"
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // Format the number using the DecimalFormat
        String formattedNumber = decimalFormat.format(graph.size());
        return formattedNumber;
    }

    public int getSolutionId() {
        return solutionId;
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    public static void solveWithDifferentAlgorithms(Puzzle puzzle) {
        // Solve using BFS
        System.out.println("**** BFS ****");
        PuzzleGraph fifteenPuzzleGraph = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph.breadthFirstSearch();

        // Solve using A* with Manhattan distance
        System.out.println("****  AStar (Manhattan)  ****");
        PuzzleGraph fifteenPuzzleGraph2 = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph2.AStarSearch(new ManhattanDistanceHeuristic());
        
        // Solve using A* with Euclidean distance
        System.out.println("**** AStar (Euclidean) ****");
        PuzzleGraph fifteenPuzzleGraph3 = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph3.AStarSearch(new EuclideanDistanceHeuristic());
        
        // Solve using A* with Zero function
        System.out.println("**** AStar (Zero func) ****");
        PuzzleGraph fifteenPuzzleGraph4 = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph4.AStarSearch(new ZeroHeuristic());

        // Solve using A* with Misplaced tiles heuristic
        System.out.println("****  AStar (Misplaced tiles)  ****");
        PuzzleGraph fifteenPuzzleGraph5 = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph5.AStarSearch(new MisplacedTilesHeuristic());
    }

    public static void main(String[] args) {
        int numOfRuns = 1;
        int n = 10;
        
        // Create 15-Puzzle with n random moves from the solution borad
        Puzzle fifteenPuzzle = new FifteenPuzzle(n);
        System.out.println(fifteenPuzzle);

        // 
        for (int i = 0; i < numOfRuns; i++) {
            PuzzleGraph.solveWithDifferentAlgorithms(fifteenPuzzle);
        }

    }
}
