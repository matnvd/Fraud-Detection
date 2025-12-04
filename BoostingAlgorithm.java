import edu.princeton.cs.algs4.Point2D;

public class BoostingAlgorithm {
    private int[][] clusteredInput; // input w/ clustered locations
    private int k; // num of clusters
    private int n; // num of transactions ("points")
    private double[] weights;
    private int[] labels;
    
    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        this.k = k;
        this.n = input.length;
        this.labels = labels;
        int[][] clusteredInput = new int[n][k];

        // iterate through each transaction and group together clusters
        for(int i = 0; i < n; i++) {
            Clustering clusteredTransaction = new Clustering(locations, k);
            clusteredInput[i] = clusteredTransaction.reduceDimensions(input[i]);
            weights[i] = 1 / n;
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm 
    public void iterate() {
        WeakLearner learnerIteration = new WeakLearner(clusteredInput, weights, labels);
    }

    // return the prediction of the learner for a new sample 
    public int predict(int[] sample) {

    }

    // unit testing
    public static void main(String[] args) {
        System.out.println("test");
    }
}