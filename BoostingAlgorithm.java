import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BoostingAlgorithm {
    private int n; // num of transactions ("points")
    private double[] weights;
    private int[] labels;
    private Clustering clusteredTransaction;
    private int[][] clusteredInput; // input w/ clustered locations
    private Queue<WeakLearner> learners;
    
    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        this.n = input.length;
        this.weights = new double[n];
        this.labels = labels.clone();

        clusteredInput = new int[n][k];
        clusteredTransaction = new Clustering(locations, k);

        learners = new Queue<>();

        // iterate through each transaction and group together clusters
        for(int i = 0; i < n; i++) {
            clusteredInput[i] = clusteredTransaction.reduceDimensions(input[i]);
            weights[i] = 1.0 / n;
        }

        System.out.println("test constructor");
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm 
    public void iterate() {
        WeakLearner learnerIteration = new WeakLearner(clusteredInput, weights, labels);
        learners.enqueue(learnerIteration);
        double[] tempWeights = weights;
        // total weight of new temp weights to re-normalize later
        int totalWeight = n;

        // iterate through each of the incorrect weights and double its weight
        for (int i = 0; i < n; i++) {
            if (learnerIteration.predict(clusteredInput[i]) != labels[i]) {
                // double wrong weights
                tempWeights[i] *= 2;
            }
            
            totalWeight += tempWeights[i];
        }

        // re-normalize weights
        for (int i = 0; i < n; i++) {
            tempWeights[i] /= totalWeight;
        }
    }

    // return the prediction of the learner for a new sample 
    public int predict(int[] sample) {
        int[] reducedSample = clusteredTransaction.reduceDimensions(sample);
        int vote0 = 0;
        int vote1 = 0;

        for(WeakLearner learner : learners) {
            if(learner.predict(reducedSample) == 0) vote0++;
            else vote1++;
        }

        if(vote0 >= vote1) return 0;
        else return 1;
    }

    // unit testing
    public static void main(String[] args) {
        // read in the terms from a file 
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(
                trainingInput, trainingLabels, trainingLocations, k
        );
        for (int t = 0; t < T; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testingAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testingAccuracy += 1;
        testingAccuracy /= testing.getN();

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testingAccuracy);
    }
}