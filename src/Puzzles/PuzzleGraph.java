package Puzzles;

import Graph.*;
import Graph.Heuristic.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PuzzleGraph {
    private Graph graph;
    private Puzzle initialPuzzle;
    private Map<Integer, Puzzle> states; // ID to Puzzle
    private boolean isSolved;

    public PuzzleGraph(Puzzle initialPuzzle) {
        this.graph = new Graph();
        this.initialPuzzle = initialPuzzle;
        this.states = new HashMap<>();
    }

    public Solution breadthFirstSearch() {
        if (!isSolved) {
            // Set start time,queue and vistied set
            long startTime = System.nanoTime();
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
                    System.out.println("\nSolved!\n");
                    isSolved = true;
                    return new Solution(graph, startTime, visited.size(), currentId, states);
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
        return null;
    }

    public Solution AStarSearch(HeuristicFunction heuristic) {
        if (!isSolved) {
            // Set start time,priority queue and vistied set
            long startTime = System.nanoTime();
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
                    System.out.println("\nSolved!\n");
                    isSolved = true;
                    return new Solution(graph, startTime, visited.size(), currentId, states);
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
        return null;
    }

    public Graph toGraph() {
        return graph;
    }

    public static void solveWithDifferentAlgorithms(Puzzle puzzle, boolean printSolutions, TimeUnit timeUnit) {
        // Solve using BFS
        System.out.println("------------------------------------- BFS -------------------------------------");
        PuzzleGraph fifteenPuzzleGraph = new PuzzleGraph(puzzle);
        Solution bfsSolution = fifteenPuzzleGraph.breadthFirstSearch();
        if (printSolutions) {
            bfsSolution.printSolution(timeUnit);
        }

        // Solve using A* with Zero function
        System.out.println("----------------------------- AStar (Zero func) ------------------------------");
        PuzzleGraph fifteenPuzzleGraph4 = new PuzzleGraph(puzzle);
        Solution astarSolution1 = fifteenPuzzleGraph4.AStarSearch(new ZeroHeuristic());
        if (printSolutions) {
            astarSolution1.printSolution(timeUnit);
        }

        // Solve using A* with Manhattan distance
        System.out.println("-----------------------------  AStar (Manhattan)  -----------------------------");
        PuzzleGraph fifteenPuzzleGraph2 = new PuzzleGraph(puzzle);
        Solution astarSolution2 = fifteenPuzzleGraph2.AStarSearch(new ManhattanDistanceHeuristic());
        if (printSolutions) {
            astarSolution2.printSolution(timeUnit);
        }

        // Solve using A* with number of misplaced tiles
        System.out.println("-------------------------- AStar (Misplaced tiles) --------------------------");
        PuzzleGraph fifteenPuzzleGraph5 = new PuzzleGraph(puzzle);
        Solution astarSolution3 = fifteenPuzzleGraph5.AStarSearch(new MisplacedTilesHeuristic());
        if (printSolutions) {
            astarSolution3.printSolution(timeUnit);
        }

        // Solve using A* with Non-Admissible heursitic
        System.out.println("------------------------------ AStar (Non-Admissible) ------------------------------");
        PuzzleGraph fifteenPuzzleGraph3 = new PuzzleGraph(puzzle);
        Solution astarSolution4 =  fifteenPuzzleGraph3.AStarSearch(new NonAdmissibleHeuristic());
        if (printSolutions) {
            astarSolution4.printSolution(timeUnit);
        }
    }

}
