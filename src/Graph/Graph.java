package Graph;

import java.util.*;

public class Graph {
    private List<Vertex> vertices;
    private int count;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.count = 0;
    }

    public int addVertex() {
        count++;
        vertices.add(new Vertex(count));
        return count;
    }

    public void addUndirectedEdge(Integer v1, Integer v2, double weight) {
        if (v1 < 1 || v1 > vertices.size() || v2 < 1 || v2 > vertices.size()) {
            throw new IllegalArgumentException("Vertex not found in the graph.");
        }

        // Add undirected edge
        vertices.get(v1 - 1).addNeighbor(vertices.get(v2 - 1), weight);
        vertices.get(v2 - 1).addNeighbor(vertices.get(v1 - 1), weight);
    }

    public Map<Integer, Integer> getDistances() {
        Map<Integer, Integer> distances = new HashMap<>();
        for (Vertex vertex : vertices) {
            distances.put(vertex.ID, vertex.getDistance());
        }
        return distances;
    }

    public Map<Integer, Integer> getPi() {
        Map<Integer, Integer> pies = new HashMap<>();
        for (Vertex vertex : vertices) {
            pies.put(vertex.ID, (vertex.getPi() != null ? vertex.getPi().ID : -1));
        }
        return pies;
    }

    public Vertex getVertex(int id) {
        if (id < 1 || id > vertices.size()) {
            throw new IllegalArgumentException("Vertex not found in the graph.");
        }
        return vertices.get(id - 1);
    }

    public Collection<Vertex> getAllVertices() {
        return new ArrayList<>(vertices);
    }

    public int size() {
        return count;
    }

    @Override
    public String toString() {
        StringBuilder graphToString = new StringBuilder();
        for (Vertex vertex : vertices) {
            graphToString.append("(").append(vertex).append(")");
            if (!vertex.getNeighbors().isEmpty()) {
                graphToString.append(" -> { ");
                for (int j = 0; j < vertex.getNeighbors().size(); j++) {
                    graphToString.append(vertex.getNeighbors().get(j));
                    if (j < vertex.getNeighbors().size() - 1) {
                        graphToString.append(",");
                    }
                }
                graphToString.append(" }");
            }
            graphToString.append("\n");
        }
        return graphToString.toString();
    }

    public static void main(String[] args) {
        // Test Graph
        Graph g = new Graph();
        for (int i = 0; i < 20; i++) {
            g.addVertex();
        }

        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int n1 = rand.nextInt(20) + 1;
            int n2 = rand.nextInt(20) + 1;
            while (n1 == n2)
                n2 = rand.nextInt(20) + 1;
            g.addUndirectedEdge(n1, n2, 0);
        }

        System.out.println(g);
    }
}
