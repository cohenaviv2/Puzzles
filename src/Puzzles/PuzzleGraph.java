package Puzzles;

import Graph.*;
import java.util.*;

public class PuzzleGraph {
    private Graph puzzleGraph;
    private Puzzle initialPuzzle;
    private Map<Integer, Puzzle> puzzleStates; // ID to Puzzle
    private int solutionId; // Store the ID of the solved puzzle state
    private boolean solved;

    public PuzzleGraph(Puzzle initialPuzzle) {
        this.puzzleGraph = new Graph();
        this.initialPuzzle = initialPuzzle;
        this.puzzleStates = new HashMap<>();
    }

    public void search_BFS() {
        if (!solved) {
            if (initialPuzzle.isSolved()) return;
            Queue<Integer> queue = new LinkedList<>();
            Set<Puzzle> visited = new HashSet<>();

            int startId = puzzleGraph.addVertex();
            puzzleStates.put(startId, initialPuzzle);
            queue.offer(startId);
            visited.add(initialPuzzle);

            while (!queue.isEmpty()) {
                int currentId = queue.poll();
                Puzzle currentPuzzle = puzzleStates.get(currentId);

                if (currentPuzzle.isSolved()) {
                    System.out.println("\nSolved!\n");
                    solutionId = currentId;
                    System.out.println("soultion id: "+solutionId+"\n");
                    return;
                }

                List<Puzzle> nextStates = currentPuzzle.generatePossibleMoves();

                for (Puzzle nextPuzzle : nextStates) {
                    if (!visited.contains(nextPuzzle)) {
                        int nextId = puzzleGraph.addVertex();
                        puzzleStates.put(nextId, nextPuzzle);
                        puzzleGraph.addUndirectedEdge(currentId, nextId, 0);

                        queue.offer(nextId);
                        visited.add(nextPuzzle);
                    }
                }
            }
        }
    }

    private void buildGraph() {
        // Add the initial puzzle state as the first vertex
        int id = puzzleGraph.addVertex();
        puzzleStates.put(id, initialPuzzle);
        boolean solved = initialPuzzle.isSolved();

        Queue<Integer> queue = new LinkedList<>();
        Set<Puzzle> visited = new HashSet<>();

        queue.offer(id);

        while (!queue.isEmpty()) {
            int currentId = queue.poll();
            Puzzle currentPuzzle = puzzleStates.get(currentId);

            // Generate possible moves for the current puzzle state
            List<Puzzle> nextPuzzles = currentPuzzle.generatePossibleMoves();

            for (Puzzle nextPuzzle : nextPuzzles) {
                // Check if the next state already exists in the graph
                if (!visited.contains(nextPuzzle)) {
                    int nextId = puzzleGraph.addVertex();
                    puzzleStates.put(nextId, nextPuzzle);
                    puzzleGraph.addUndirectedEdge(currentId, nextId, 0);

                    // Check if the next state is the solved state
                    // if (nextPuzzle.isSolved()) {
                    // targetVertexId = nextId;
                    // solved = true;
                    // System.out.println("** THIS IS THE SOLVED PUZZLE **");
                    // }

                    queue.offer(nextId);
                    visited.add(nextPuzzle);
                    // System.out.println(nextPuzzle);

                    // if (nextPuzzle.isSolved()) {
                    // targetVertexId = nextId;
                    // solved = true;
                    // System.out.println("** THIS IS THE SOLVED PUZZLE **");
                    // System.out.println(nextPuzzle);
                    // break;
                    // } else {
                    // queue.offer(nextId);
                    // visited.add(nextPuzzle);
                    // System.out.println(nextPuzzle);

                    // }
                }
            }
        }
        System.out.println("Number of vertices: " + this.puzzleGraph.getAllVertices().size());
    }

    public Graph toGraph() {
        return puzzleGraph;
    }

    public int getNumOfVertices() {
        return puzzleGraph.getAllVertices().size();
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
        Puzzle puzzle15 = new FifteenPuzzle(5000);
        System.out.println(puzzle15);

        // Solve puzzle using BFS
        PuzzleGraph puzzle15Graph = new PuzzleGraph(puzzle15);
        long startTime = System.currentTimeMillis();
        puzzle15Graph.search_BFS();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Convert milliseconds to seconds with dot and milliseconds
        double secondsWithDot = elapsedTime / 1000.0;
        System.out.printf("Time: %.3f seconds%n", secondsWithDot);
        System.out.println("Number of vertices: " + puzzle15Graph.getNumOfVertices()+"\n");

        // // Create 24-Puzzle with 50 random moves from the solution borad
        // Puzzle puzzle24 = new TwentyFourPuzzle(50);
        // System.out.println(puzzle24);

        // // Solve puzzle using BFS
        // PuzzleGraph puzzle24Graph = new PuzzleGraph(puzzle24);
        // startTime = System.currentTimeMillis();
        // puzzle24Graph.search_BFS();
        // endTime = System.currentTimeMillis();
        // elapsedTime = endTime - startTime;

        // // Convert milliseconds to seconds with dot and milliseconds
        // secondsWithDot = elapsedTime / 1000.0;
        // System.out.printf("Time: %.3f seconds%n", secondsWithDot);
        // System.out.println("Number of vertices: " + puzzle24Graph.getNumOfVertices());
    }
}
