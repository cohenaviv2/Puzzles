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

    public void addUndirectedEdge(Integer v1, Integer v2) {
        if (v1-1 < 0 || v1-1 >= vertices.size() || v2-1 < 0 || v2-1 >= vertices.size()) {
            throw new IllegalArgumentException("Vertex not found in the graph.");
        }

        // Add undirected edge
        vertices.get(v1 - 1).addNeighbor(vertices.get(v2 - 1));
        vertices.get(v2 - 1).addNeighbor(vertices.get(v1 - 1));
    }

    public Map<Integer, Integer> getDistances() {
        Map<Integer, Integer> distances = new HashMap<>();
        for (Vertex vertex : vertices) {
            distances.put(vertex.ID, vertex.getG());
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
        if (id-1 < 0 || id-1 >= vertices.size()) {
            throw new IllegalArgumentException("Vertex not found in the graph: "+id);
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
}
