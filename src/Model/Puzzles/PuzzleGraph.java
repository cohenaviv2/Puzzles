package Model.Puzzles;

import java.util.*;
import java.util.concurrent.TimeUnit;

import Model.Graph.*;
import Model.Graph.Heuristic.*;

/* 
 * This class represent a nXn Puzzle solver, using BFS and AStar algorithms.
 * These search algorithms solving the puzzle by converting the problem into an undirected graph.
 * The graph is created dynamically as the search performed on the puzzle,
 * And finally returns a Solution.
 * Class also contains a static method to solve a specific puzzle with all the algorithms.
 * 
 * @author: Aviv Cohen
 * 
 */

public class PuzzleGraph {
    private Graph graph;
    private Map<Integer, Puzzle> states; // Vertex ID to Puzzle

    public PuzzleGraph() {
        this.graph = new Graph();
        this.states = new HashMap<>();
    }

    public Solution breadthFirstSearch(Puzzle puzzle) {
        // Set start time, queue of vertex.ID and 'vistied' set
        long startTime = System.nanoTime();
        Queue<Integer> queue = new LinkedList<>(); // vertex id
        Set<Puzzle> visited = new HashSet<>();

        // Create first vertex
        int startId = graph.addVertex();
        states.put(startId, puzzle);

        // Set predeessor and add to the queue & visited
        graph.getVertex(startId).setPi(null);
        queue.offer(startId);
        visited.add(puzzle);

        while (!queue.isEmpty()) {
            int currentId = queue.poll();
            Puzzle currentPuzzle = states.get(currentId);

            // Algorithm requires a stopping condition (solutions space is exponential)
            if (currentPuzzle.isSolved()) {
                Solution solution = new Solution(graph, startTime, currentId, states);
                clear();
                return solution;
            }

            // Generate possible board states
            List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();
            for (Puzzle nextPuzzle : nextStates) {
                if (!visited.contains(nextPuzzle)) { // Board state doesnt discovered yet
                    // Create the next state vertex
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
        // Set start time, priority queue of vertiex.ID and 'vistied' set
        long startTime = System.nanoTime();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(id -> graph.getVertex(id).getF()));
        Set<Puzzle> visited = new HashSet<>();

        // Create starting vertex
        int startId = graph.addVertex();
        Vertex startVertex = graph.getVertex(startId);

        // Set predeessor, 'g' & 'h' scores and add to the queue & visited
        startVertex.setPi(null);
        startVertex.setG(0);
        startVertex.setH(heuristic.calculate(puzzle));
        states.put(startId, puzzle);
        priorityQueue.offer(startId);
        visited.add(puzzle);

        while (!priorityQueue.isEmpty()) {
            int currentId = priorityQueue.poll();
            Vertex currentVertex = graph.getVertex(currentId);
            Puzzle currentPuzzle = states.get(currentId);

            if (currentPuzzle.isSolved()) {
                Solution solution = new Solution(graph, startTime, currentId, states);
                clear();
                return solution;
            }

            // Generate possible board states
            List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();
            for (Puzzle nextPuzzle : nextStates) {
                if (!visited.contains(nextPuzzle)) { // Board state doesnt discovered yet
                    // Create the next state vertex
                    int nextId = graph.addVertex();
                    Vertex nextVertex = graph.getVertex(nextId);
                    graph.addUndirectedEdge(currentVertex.ID, nextVertex.ID);
                    nextVertex.setPi(currentVertex);
                    int gScore = currentVertex.getG() + 1; // All edges have weight 1
                    nextVertex.setG(gScore);
                    nextVertex.setH(heuristic.calculate(nextPuzzle));
                    states.put(nextId, nextPuzzle);
                    visited.add(nextPuzzle);
                    // Add to the priority queue, updating the queue
                    priorityQueue.offer(nextId);
                }
            }
        }
        return null;
    }

    private void clear() {
        this.graph = new Graph();
        this.states.clear();
    }

    /* METHOD FOR TESTING */
    public static List<Solution> solveWithDifferentAlgorithms(Puzzle puzzle, boolean print, TimeUnit timeUnit) {
        // Create Puzzle Graph
        PuzzleGraph puzzleGraph = new PuzzleGraph();
        List<Solution> solutionsList = new ArrayList<>();

        // Solve using BFS
        Solution bfsSolution = puzzleGraph.breadthFirstSearch(puzzle);
        solutionsList.add(bfsSolution);
        if (print) {
            System.out.println("------------------------------------- BFS -------------------------------------");
            System.out.println(bfsSolution.toString(timeUnit));
        }

        // Solve using A* with Zero function
        Solution astarSolution1 = puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Zero_Heuristic);
        solutionsList.add(astarSolution1);
        if (print) {
            System.out.println("----------------------------- AStar (Zero func) ------------------------------");
            System.out.println(astarSolution1.toString(timeUnit));
        }

        // Solve using A* with Manhattan distance
        Solution astarSolution2 = puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Manhattan_Distance);
        solutionsList.add(astarSolution2);
        if (print) {
            System.out.println("-----------------------------  AStar (Manhattan)  -----------------------------");
            System.out.println(astarSolution2.toString(timeUnit));
        }

        // Solve using A* with Euclidean distance
        Solution astarSolution5 = puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Euclidean_Distance);
        solutionsList.add(astarSolution5);
        if (print) {
            System.out.println("-----------------------------  AStar (Euclidean)  -----------------------------");
            System.out.println(astarSolution5.toString(timeUnit));
        }

        // Solve using A* with number of misplaced tiles
        Solution astarSolution3 = puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Misplaced_Tiles);
        solutionsList.add(astarSolution3);
        if (print) {
            System.out.println("-------------------------- AStar (Misplaced tiles) --------------------------");
            System.out.println(astarSolution3.toString(timeUnit));
        }

        // Solve using A* with Non-Admissible heursitic
        Solution astarSolution4 = puzzleGraph.AStarSearch(puzzle, PuzzleHeuristic.Permutations_Inversions);
        solutionsList.add(astarSolution4);
        if (print) {
            System.out.println("-------------------------- AStar (Permutation inversions) --------------------------");
            System.out.println(astarSolution4.toString(timeUnit));
        }

        return solutionsList;
    }

}
