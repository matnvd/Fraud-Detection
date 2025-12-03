import java.util.Arrays;

import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdRandom;

public class Clustering {

    private CC components; // components of the updated graph
    private int k; // number of clusters
    private int m; // number of locations

    // run the clustering algorithm and create the clusters  
    public Clustering(Point2D[] locations, int k) {
        if (locations == null) throw new IllegalArgumentException();
        
        this.k = k;
        m = locations.length;

        if (k < 1 || k > m) throw new IllegalArgumentException();

        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);
        for(int i = 0; i < m; i++) {
            for(int j = i + 1; j < m; j++) {
                graph.addEdge(new Edge(i, j, locations[i].distanceTo(locations[j])));
            }
        }

        KruskalMST mst = new KruskalMST(graph);

        Edge[] edges = new Edge[m - 1];
        int ix = 0;
        for(Edge edge : mst.edges()) {
            edges[ix] = edge;
            ix++;
        }

        Arrays.sort(edges);

        EdgeWeightedGraph fgraph = new EdgeWeightedGraph(m);
        for(int i = 0; i < m - k; i++) {
            fgraph.addEdge(edges[i]);
        }

        components = new CC(fgraph);
    }

    // return the cluster of the ith location 
    public int clusterOf(int i) {
        if (i < 0 || i > m - 1) throw new IllegalArgumentException();
        
        return components.id(i);
    }

    // use the clusters to reduce the dimensions of an input 
    public int[] reduceDimensions(int[] input) {
        if (input == null) throw new IllegalArgumentException();

        int[] out = new int[k];
        for(int i = 0; i < input.length; i++) {
            out[clusterOf(i)] += input[i];
        }

        return out;
    }

    // unit testing (required)
    public static void main(String[] args) {
        int c = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);

        Point2D[] centers = new Point2D[c];
        for(int i = 0; i < c; i++) {
            if(i == 0) {
                double randX = StdRandom.uniformDouble(0.0, 1000.0);
                double randY = StdRandom.uniformDouble(0.0, 1000.0);
                Point2D randPt = new Point2D(randX, randY);
                centers[i] = randPt;
            } else {
                double minDist = 0;
                while (minDist < 4) {
                    double randX = StdRandom.uniformDouble(0.0, 1000.0);
                    double randY = StdRandom.uniformDouble(0.0, 1000.0);
                    Point2D randPt = new Point2D(randX, randY);
                    centers[i] = randPt;

                    minDist = Double.MAX_VALUE;
                    for(int j = 0; j < i; j++) {
                        double dist = centers[i].distanceTo(centers[j]);
                        if(dist < minDist) minDist = dist;
                    }
                }
            }
        }

        Point2D[] locations = new Point2D[c * p];
        for(int i = 0; i < c; i++) {
            for (int j = 0; j < p; j++) {
                double dist = 2;
                while (dist > 1) {
                    double randX = StdRandom.uniformDouble(centers[i].x() - 1, centers[i].x() + 1);
                    double randY = StdRandom.uniformDouble(centers[i].y() - 1, centers[i].y() + 1);
                    Point2D randPt = new Point2D(randX, randY);
                    dist = randPt.distanceTo(centers[i]);
                    locations[i * p + j] = randPt;
                }
            }
        }

        Clustering clusterTest = new Clustering(locations, c);
        for(int i = 0; i < c * p; i++) {
            // compares each location in cluster to the first location in the cluster
            int firstLocInCluster = i - i % p;
            if (clusterTest.clusterOf(i) != clusterTest.clusterOf(firstLocInCluster)) {
                System.out.println("Error!");
            }
        }
    }
}