package Graph;

import java.util.*;

public class Vertex {
    public final int ID;
    private List<Vertex> adj; // Neighbor to Weight
    private Vertex pi;
    private int distance;
    private int heuristic;

    public Vertex(int id) {
        ID = id;
        this.adj = new ArrayList<>();
    }

    public List<Vertex> getNeighbors() {
        return Collections.unmodifiableList(new ArrayList<>(adj));
    }    

    public void addNeighbor(Vertex neighbor) {
        if (!adj.contains(neighbor) || neighbor != this) {
            adj.add(neighbor);
        }
    }

    // public double getWeight(Vertex neighbor) {
    //     if (adj.contains(neighbor)) {
    //         return adj.get(neighbor);
    //     } else {
    //         return Double.MAX_VALUE;
    //     }
    // }

    // public void setWeight(Vertex neighbor, double weight) {
    //     adj.put(neighbor, weight);
    // }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Vertex getPi() {
        return pi;
    }

    public void setPi(Vertex predecessor) {
        this.pi = predecessor;
    }

    public int getFunction() {
        return distance + heuristic;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
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
        return true;
    }

}
