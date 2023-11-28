package Graph;

import java.util.*;

/* 
 * This class represents a vertex in the graph.
 * Vertex contains a list of its neighbors,
 * its predecessor as 'pi', and 'g' & 'h' scores for AStar algorithm.
 * 
 * @author: Aviv Cohen
 * 
 */

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
    
    public void addNeighbor(Vertex neighbor) {
        if (!adj.contains(neighbor) || neighbor != this) {
            adj.add(neighbor);
        }
    }

    public List<Vertex> getNeighbors() {
        return Collections.unmodifiableList(new ArrayList<>(adj));
    }    

    public Vertex getPi() {
        return pi;
    }

    public void setPi(Vertex predecessor) {
        this.pi = predecessor;
    }

    public void setG(int distance) {
        this.g = distance;
    }

    public int getG() {
        return g;
    }

    public void setH(double heuristic) {
        this.h = heuristic;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return g + h;
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

    public void clear() {
        adj.clear();
    }
}
