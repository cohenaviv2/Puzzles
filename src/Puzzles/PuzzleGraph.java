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
    private List<Integer> path;

    public PuzzleGraph(Puzzle initialPuzzle) {
        this.graph = new Graph();
        this.initialPuzzle = initialPuzzle;
        this.states = new HashMap<>();
    }

    public void breadthFirstSearch() {
        if (!isSolved) {
            // Set start time,queue and vistied set
            startTime = System.currentTimeMillis();
            Queue<Integer> queue = new LinkedList<>(); // vertex id
            Set<Puzzle> visited = new HashSet<>();

            // Create starting vertex
            int startId = graph.addVertex();
            states.put(startId, initialPuzzle);

            // Set pi,visited & Add to the queue
            graph.getVertex(startId).setPi(null);
            queue.offer(startId);
            visited.add(initialPuzzle);

            while (!queue.isEmpty()) {
                int currentId = queue.poll();
                Puzzle currentPuzzle = states.get(currentId);

                // Algorithm requires a stopping condition (solution space is exponential)
                if (currentPuzzle.isSolved()) {
                    solutionId = currentId;
                    printSolution(visited.size());
                    isSolved = true;
                    return;
                }

                // Generate possible board states
                List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();
                for (Puzzle nextPuzzle : nextStates) {
                    if (!visited.contains(nextPuzzle)) { // State not discovered yet
                        // Create possible state vertex
                        int nextId = graph.addVertex();
                        graph.addUndirectedEdge(currentId, nextId);
                        graph.getVertex(nextId).setPi(graph.getVertex(currentId));
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
            // Set start time,priority queue and vistied set
            startTime = System.currentTimeMillis();
            PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(
                    Comparator.comparingDouble(id -> graph.getVertex(id).getF()));
            Set<Puzzle> visited = new HashSet<>();

            // Create starting vertex
            int startId = graph.addVertex();
            Vertex startVertex = graph.getVertex(startId);

            // Set pi,g,h,visited & Add to the queue
            startVertex.setPi(null);
            startVertex.setG(0);
            startVertex.setH(heuristic.calculate(initialPuzzle));
            states.put(startId, initialPuzzle);
            priorityQueue.offer(startId);
            visited.add(initialPuzzle);

            while (!priorityQueue.isEmpty()) {
                int currentId = priorityQueue.poll();
                Vertex currentVertex = graph.getVertex(currentId);
                Puzzle currentPuzzle = states.get(currentId);



                if (currentPuzzle.isSolved()) {
                    solutionId = currentId;
                    int developedVertices = visited.size();
                    printSolution(developedVertices);
                    isSolved = true;
                    return;
                }

                List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();

                for (Puzzle nextPuzzle : nextStates) {
                    int nextId = graph.addVertex();
                    Vertex nextVertex = graph.getVertex(nextId);
                    nextVertex.setPi(currentVertex);
                    int tentativeG = currentVertex.getG() + 1; // All edges have weight 1

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

    private void printSolution(int developedVertices) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nSolved!\n\n");
        sb.append("Vertices in the Graph: " + getNumOfVertices() + "\n");
        //
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        // Format the number using the DecimalFormat
        String formattedNumber = decimalFormat.format(developedVertices);
        sb.append("Number of developed vertices: " + formattedNumber + "\n");
        sb.append("Time: " + elapsedTime() + " seconds\n");
        //
        sb.append("Heap Memory Usage: " + memoryUsage() + " MB\n");
        sb.append("Soultion vertex ID: " + solutionId + "\n");
        path = new ArrayList<>();
        int currentId = solutionId;
        while (currentId != -1) {
            path.add(currentId);
            currentId = graph.getVertex(currentId).getPi() != null ? graph.getVertex(currentId).getPi().ID : -1;
        }

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
        sb.append("Number of movements in the solution path: " + (path.size()-1) + "\n");
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

    private String getNumOfVertices() {
        // Create a DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(graph.size());
        return formattedNumber;
    }

    public Graph toGraph() {
        return graph;
    }

    public int getSolutionId() {
        return isSolved ? solutionId : -1;
    }

    public void printMovesToSolution() {
        if (isSolved) {
            System.out.println("Moves to Solution:\n");
            Timer timer = new Timer();
            int totalSteps = path.size();

            for (int i = totalSteps - 1; i >= 0; i--) {
                final int stepIndex = i;

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int vertexId = path.get(stepIndex);
                        System.out.println("\nStep " + (totalSteps - stepIndex) + ":\n" + states.get(vertexId));

                        if (stepIndex == 0) {
                            timer.cancel();
                        }
                    }
                }, (totalSteps - stepIndex) * 1000);
            }
        }
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    public static void solveWithDifferentAlgorithms(Puzzle puzzle) {
        // Solve using BFS
        System.out.println("------------------------------------- BFS -------------------------------------");
        PuzzleGraph fifteenPuzzleGraph = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph.breadthFirstSearch();

        // // Solve using A* with Misplaced tiles heuristic
        // System.out.println("--------------------------  AStar (Misplaced tiles) --------------------------");
        // PuzzleGraph fifteenPuzzleGraph5 = new PuzzleGraph(puzzle);
        // fifteenPuzzleGraph5.AStarSearch(new MisplacedTilesHeuristic());

        // Solve using A* with Euclidean distance
        System.out.println("------------------------------ AStar (Euclidean) ------------------------------");
        PuzzleGraph fifteenPuzzleGraph3 = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph3.AStarSearch(new EuclideanDistanceHeuristic());

        // Solve using A* with Manhattan distance
        System.out.println("-----------------------------  AStar (Manhattan)  -----------------------------");
        PuzzleGraph fifteenPuzzleGraph2 = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph2.AStarSearch(new ManhattanDistanceHeuristic());

        // Solve using A* with Zero function
        System.out.println("----------------------------- AStar (Zero func) ------------------------------");
        PuzzleGraph fifteenPuzzleGraph4 = new PuzzleGraph(puzzle);
        fifteenPuzzleGraph4.AStarSearch(new ZeroHeuristic());

    }

}
