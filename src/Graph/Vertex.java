package Graph;

import java.util.*;
import java.util.stream.Collectors;

public class Vertex {
    public final int ID;
    private final Map<Vertex, Double> adj; // Neighbor to Weight
    // Search
    private Vertex pi;
    private int d;
    private String color;
    private int h;

    public Vertex(int id) {
        ID = id;
        this.adj = new HashMap<>();
    }

    public List<Vertex> getNeighbors() {
        return adj.keySet().stream().sorted((v1, v2) -> v1.ID - v2.ID).collect(Collectors.toList());
    }

    public void addNeighbor(Vertex neighbor, double weight) {
        if (!adj.containsKey(neighbor) || neighbor != this) {
            adj.put(neighbor, weight);
        }
    }

    public double getWeight(Vertex neighbor) {
        if (adj.containsKey(neighbor)) {
            return adj.get(neighbor);
        } else {
            return Double.MAX_VALUE;
        }
    }

    public void setWeight(Vertex neighbor, double weight) {
        adj.put(neighbor, weight);
    }

    public int getDistance() {
        return d;
    }

    public void setDistance(int distance) {
        this.d = distance;
    }

    public Vertex getPi() {
        return pi;
    }

    public void setPi(Vertex predecessor) {
        this.pi = predecessor;
    }

    public int getFunction() {
        return d + h;
    }

    public int getHeuristic() {
        return h;
    }

    public void setHeuristic(int heuristic) {
        this.h = heuristic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.valueOf(ID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (ID != other.ID)
            return false;
        if (adj == null) {
            if (other.adj != null)
                return false;
        } else if (!adj.equals(other.adj))
            return false;
        return true;
    }

}
