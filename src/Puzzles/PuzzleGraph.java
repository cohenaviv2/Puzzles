package Puzzles;

import Graph.*;
import Graph.Heuristic.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PuzzleGraph {
    private Graph graph;
    private Puzzle initialPuzzle;
    private Map<Integer, Puzzle> states; // ID to Puzzle

    public PuzzleGraph() {
        this.graph = new Graph();
        this.states = new HashMap<>();
    }

    public Solution breadthFirstSearch(Puzzle puzzle) {
        this.initialPuzzle = puzzle;
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
                Solution solution = new Solution(graph, startTime, visited.size(), currentId, states);
                clear();
                return solution;
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
                    visited.add(nextPuzzle);
                    queue.offer(nextId);
                }
            }
        }
        return null;
    }

    public Solution AStarSearch(Puzzle puzzle, PuzzleHeuristic heuristic) {
        this.initialPuzzle = puzzle;
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
                Solution solution = new Solution(graph, startTime, visited.size(), currentId, states);
                clear();
                return solution;
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
                    graph.addUndirectedEdge(currentVertex.ID, nextVertex.ID);
                    visited.add(nextPuzzle);
                    priorityQueue.offer(nextId);
                }
            }
        }
        return null;
    }

    private void clear() {
        this.graph.clear();
        this.states.clear();
    }

    public static List<Solution> solveWithDifferentAlgorithms(Puzzle puzzle, boolean print, TimeUnit timeUnit) {
        // Create Puzzle Graph
        PuzzleGraph puzzleGraph = new PuzzleGraph();
        List<Solution> solutionsList = new ArrayList<>();

        // Solve using BFS
        Solution bfsSolution = puzzleGraph.breadthFirstSearch(puzzle);
        solutionsList.add(bfsSolution);
        if (print) {
            System.out.println("------------------------------------- BFS -------------------------------------");
            bfsSolution.print(timeUnit);
        }

        // Solve using A* with Zero function
        Solution astarSolution1 = puzzleGraph.AStarSearch(puzzle, Heuristics.Zero_Heuristic);
        solutionsList.add(astarSolution1);
        if (print) {
            System.out.println("----------------------------- AStar (Zero func) ------------------------------");
            astarSolution1.print(timeUnit);
        }

        // Solve using A* with Manhattan distance
        Solution astarSolution2 = puzzleGraph.AStarSearch(puzzle, Heuristics.Manhattan_Distance);
        solutionsList.add(astarSolution2);
        if (print) {
            System.out.println("-----------------------------  AStar (Manhattan)  -----------------------------");
            astarSolution2.print(timeUnit);
        }

        //         // Solve using A* with Euclidean distance
        // Solution astarSolution5 = puzzleGraph.AStarSearch(puzzle, Heuristics.Euclidean_Distance);
        // solutionsList.add(astarSolution5);
        // if (print) {
            // System.out.println("-----------------------------  AStar (Euclidean)  -----------------------------");
        //     astarSolution5.print(timeUnit);
        // }

        // // Solve using A* with number of misplaced tiles
        // Solution astarSolution3 = puzzleGraph.AStarSearch(puzzle, Heuristics.Misplaced_Tiles);
        // solutionsList.add(astarSolution3);
        // if (print) {
            // System.out.println("-------------------------- AStar (Misplaced tiles) --------------------------");
        //     astarSolution3.print(timeUnit);
        // }

        // Solve using A* with Non-Admissible heursitic
        Solution astarSolution4 = puzzleGraph.AStarSearch(puzzle, Heuristics.Nilsson_Sequence);
        solutionsList.add(astarSolution4);
        if (print) {
            System.out.println("-------------------------- AStar (Nilsson's Sequence) --------------------------");
            astarSolution4.print(timeUnit);
        }

        return solutionsList;
    }

}
