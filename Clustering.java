import java.util.Arrays;

import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;

public class Clustering {

    private CC components;
    private int k;

    // run the clustering algorithm and create the clusters  
    public Clustering(Point2D[] locations, int k) {
        this.k = k;

        EdgeWeightedGraph graph = new EdgeWeightedGraph(locations.length);
        for(int i = 0; i < locations.length; i++) {
            for(int j = i+1; j < locations.length; j++) {
                graph.addEdge(new Edge(i, j, locations[i].distanceTo(locations[j])));
            }
        }

        KruskalMST mst = new KruskalMST(graph);

        Edge[] edges = new Edge[locations.length - 1];
        int ix = 0;
        for(Edge edge : mst.edges()) {
            edges[ix] = edge;
            ix++;
        }

        Arrays.sort(edges);

        EdgeWeightedGraph fgraph = new EdgeWeightedGraph(locations.length);
        for(int i = 0; i < k; i++) {
            fgraph.addEdge(edges[i]);
        }

        components = new CC(fgraph);
    }

    // return the cluster of the ith location 
    public int clusterOf(int i) {
        return components.id(i);
    }

    // use the clusters to reduce the dimensions of an input 
    public int[] reduceDimensions(int[] input) {
        int[] out = new int[k];
        for(int i = 0; i < input.length; i++) {
            out[clusterOf(i)] += input[i];
        }

        return out;
    }

    // unit testing (required)
    public static void main(String[] args) {

    }
}