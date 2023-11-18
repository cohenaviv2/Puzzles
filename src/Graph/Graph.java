package Graph;

import java.util.*;

public class Graph {
    private Map<Integer, Vertex> vertices; // ID to Vertex
    private int count;

    public Graph() {
        this.vertices = new HashMap<>();
        this.count = -1;
    }

    public int addVertex() {
        this.count++;
        vertices.put(count, new Vertex(count));
        return count;
    }

    public void addUndirectedEdge(Integer v1, Integer v2, double weight) {
        if (!vertices.containsKey(v1) || !vertices.containsKey(v2)) {
            throw new IllegalArgumentException("Source or destination vertex not found in the graph.");
        }
        // Get vertices
        Vertex vertex1 = vertices.get(v1);
        Vertex vertex2 = vertices.get(v2);
        // Add undirected edge
        vertex1.addNeighbor(vertex2,weight);
        vertex2.addNeighbor(vertex1,weight);
    }

    public Map<Integer, Integer> getDistances() {
        // Get Distances map after performing search algorithm
        Map<Integer, Integer> distances = new HashMap<>(); // ID to Distance
        for (Vertex vertex : vertices.values()) {
            distances.put(vertex.ID, vertex.getDistance());
        }
        return distances;
    }

    public Map<Integer, Integer> getPi() {
        // Get Distances map after performing search algorithm
        Map<Integer, Integer> pies = new HashMap<>(); // ID to Distance
        for (Vertex vertex : vertices.values()) {
            pies.put(vertex.ID, (vertex.getPi()!= null ? vertex.getPi().ID : -1));
        }
        return pies;
    }

    public Vertex getVertex(int id) {
        if (!vertices.containsKey(id)) {
            throw new IllegalArgumentException("Vertex not found in the graph.");
        }
        return vertices.get(id);
    }

    public Collection<Vertex> getAllVertices() {
        return vertices.values();
    }

    public void dfs() {
        Set<Integer> visited = new HashSet<>();
        for (Vertex vertex : vertices.values()) {
            if (!visited.contains(vertex.ID)) {
                dfsHelper(vertex, visited);
            }
        }
    }

    private void dfsHelper(Vertex vertex, Set<Integer> visited) {
        visited.add(vertex.ID);
        System.out.println("Visited: " + vertex.ID);

        for (Vertex neighbor : vertex.getNeighbors()) {
            if (!visited.contains(neighbor.ID)) {
                dfsHelper(neighbor, visited);
            }
        }
    }

    @Override
    public String toString() {
        String graphToString = "";
        for (Vertex vertex : vertices.values()) {
            graphToString += ("(" + vertex + ")");
            if (vertex.getNeighbors().size() > 0) {
                graphToString += " -> { ";
                for (int i = 0; i < vertex.getNeighbors().size(); i++) {
                    graphToString += (vertex.getNeighbors().get(i));
                    if (i<vertex.getNeighbors().size()-1){
                        graphToString += ",";
                    }
                }
                graphToString += " }";
            }
            graphToString += "\n";
        }
        return graphToString;
    }

    public static void main(String[] args) {
        // Test Graph
        Graph g = new Graph();
        for (int i = 0; i < 20; i++) {
            g.addVertex();
        }

        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int n1 = rand.nextInt(20);
            int n2 = rand.nextInt(20);
            while (n1 == n2)
                n2 = rand.nextInt(20);
            g.addUndirectedEdge(n1, n2,0);
        }

        int id0 = 0;
        Vertex first = g.getVertex(id0);
        
        Collection<Vertex> vertices = g.getAllVertices();
        System.out.println(g);

    }
}
