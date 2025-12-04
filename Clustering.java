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
        // initializes number of clusters and locations and throws invalid args
        if (locations == null) throw new IllegalArgumentException();
        
        this.k = k;
        m = locations.length;

        if (k < 1 || k > m) throw new IllegalArgumentException();

        // creates graph with edges between each location
        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                graph.addEdge(new Edge(i, j, locations[i].distanceTo(locations[j])));
            }
        }

        // finds MST of the graph
        KruskalMST mst = new KruskalMST(graph);

        Edge[] edges = new Edge[m - 1];
        int ix = 0;
        for (Edge edge : mst.edges()) {
            edges[ix] = edge;
            ix++;
        }

        // creates new graph with first m - k edges
        Arrays.sort(edges);

        EdgeWeightedGraph clusterGraph = new EdgeWeightedGraph(m);
        for (int i = 0; i < m - k; i++) {
            clusterGraph.addEdge(edges[i]);
        }
    
        components = new CC(clusterGraph);
    }

    // return the cluster of the ith location 
    public int clusterOf(int i) {
        if (i < 0 || i > m - 1) throw new IllegalArgumentException();
        
        return components.id(i);
    }

    // use the clusters to reduce the dimensions of an input 
    public int[] reduceDimensions(int[] input) {
        if (input == null || input.length != m) throw new IllegalArgumentException();

        // add each input to their respective components index in the out[]
        int[] out = new int[k];
        for (int i = 0; i < input.length; i++) {
            out[clusterOf(i)] += input[i];
        }

        return out;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // c: number of centers, p: number of points per cluster
        int c = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);

        // stores centers for which points will be clustered around
        Point2D[] centers = new Point2D[c];
        for (int i = 0; i < c; i++) {
            // generates a random point, making sure its >= 4 away from other centers
            if (i == 0) {
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

                    // makes sure its far enough away from all other centers
                    minDist = Double.POSITIVE_INFINITY;
                    for (int j = 0; j < i; j++) {
                        double dist = centers[i].distanceTo(centers[j]);
                        if (dist < minDist) minDist = dist;
                    }
                }
            }
        }

        // stores locations of all points
        Point2D[] locations = new Point2D[c * p];
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < p; j++) {
                double dist = 2;
                while (dist > 1) {
                    // generates a random point +- 1 unit away from the center
                    // calculates distance from center, if too far, generates again
                    double cx = centers[i].x();
                    double cy = centers[i].y();
                    double randX = StdRandom.uniformDouble(cx - 1, cx + 1);
                    double randY = StdRandom.uniformDouble(cy - 1, cy + 1);
                    Point2D randPt = new Point2D(randX, randY);
                    dist = randPt.distanceTo(centers[i]);
                    locations[i * p + j] = randPt;
                }
            }
        }

        Clustering clusterTest = new Clustering(locations, c);
        for (int i = 0; i < c * p; i++) {
            // compares each location in cluster to the first location in the cluster
            int firstInCluster = i - i % p;
            if (clusterTest.clusterOf(i) != clusterTest.clusterOf(firstInCluster)) {
                System.out.println("Error!");
            }
        }
    }
}