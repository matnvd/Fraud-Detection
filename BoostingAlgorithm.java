import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;

public class BoostingAlgorithm {
    private int n; // num of transactions ("points")
    private int m; // num of locations
    private double[] weights; // current weights
    private int[] labels; // actual labels for each position
    private Clustering clusteredTransaction; // clustering for reference in predict()
    private int[][] clusteredInput; // input w/ clustered locations
    private Queue<WeakLearner> learners; // a list of all previous
    
    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        if (input == null || labels == null || locations == null) {
            throw new IllegalArgumentException();
        }

        if (input.length == 0) throw new IllegalArgumentException();

        if (labels.length != input.length || input[0].length != locations.length) {
            throw new IllegalArgumentException();
        }

        if (k < 1 || k > locations.length) throw new IllegalArgumentException();

        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != 0 && labels[i] != 1) {
                throw new IllegalArgumentException();
            }
        }

        // initializes variables and stores in instance variables
        this.n = input.length;
        this.m = locations.length;
        this.weights = new double[n];
        this.labels = labels.clone();

        clusteredInput = new int[n][k];
        clusteredTransaction = new Clustering(locations, k);

        learners = new Queue<>();

        // iterate through each transaction and group together clusters
        for (int i = 0; i < n; i++) {
            // clusters each transaction and adds to new array
            clusteredInput[i] = clusteredTransaction.reduceDimensions(input[i]);
            // weights initialized to 1 / n
            weights[i] = 1.0 / n;
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        if (i < 0 || i > n - 1) throw new IllegalArgumentException();

        return weights[i];
    }

    // apply one step of the boosting algorithm 
    public void iterate() {
        // creates a weakLearner
        WeakLearner newLearner = new WeakLearner(clusteredInput, weights, labels);
        learners.enqueue(newLearner);
        double[] tempWeights = weights;
        // total weight of new temp weights to re-normalize later
        double totalWeight = 0;

        // iterate through each of the incorrect weights and double its weight
        for (int i = 0; i < n; i++) {
            if (newLearner.predict(clusteredInput[i]) != labels[i]) {
                // double wrong weights
                tempWeights[i] *= 2;
            }
            
            totalWeight += tempWeights[i];
        }

        // re-normalizes weights
        for (int i = 0; i < n; i++) {
            tempWeights[i] /= totalWeight;
        }

        weights = tempWeights;
    }

    // return the prediction of the learner for a new sample 
    public int predict(int[] sample) {
        if (sample == null) throw new IllegalArgumentException();
        if (sample.length != m) throw new IllegalArgumentException();

        // clusters sample
        int[] reducedSample = clusteredTransaction.reduceDimensions(sample);

        // counts up votes from each learner for the specific sample
        int vote0 = 0;
        int vote1 = 0;

        for (WeakLearner learner : learners) {
            if (learner.predict(reducedSample) == 0) vote0++;
            else vote1++;
        }
        
        // returns majority vote, 0 in case of tie
        if (vote0 >= vote1) return 0;
        else return 1;
    }

    // unit testing
    public static void main(String[] args) {
        
    }
}