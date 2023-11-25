package Graph;

import java.util.*;

public class Vertex {
    public final int ID;
    private List<Vertex> adj; // Neighbor to Weight
    private Vertex pi;
    private int g;
    private double h;

    public Vertex(int id) {
        ID = id;
        this.adj = new ArrayList<>();
        this.pi = null;
    }

    public List<Vertex> getNeighbors() {
        return Collections.unmodifiableList(new ArrayList<>(adj));
    }    

    public void addNeighbor(Vertex neighbor) {
        if (!adj.contains(neighbor) || neighbor != this) {
            adj.add(neighbor);
        }
    }

    public int getG() {
        return g;
    }

    public void setG(int distance) {
        this.g = distance;
    }

    public Vertex getPi() {
        return pi;
    }

    public void setPi(Vertex predecessor) {
        this.pi = predecessor;
    }

    public double getF() {
        return g + h;
    }

    public double getH() {
        return h;
    }

    public void setH(double heuristic) {
        this.h = heuristic;
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
