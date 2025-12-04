import java.util.Arrays;
import java.util.Comparator;

public class WeakLearner {

    // instance variables for predictor, sp: sign, dp: dimension, vp: value
    private int sp, vp, dp;
    private int k; // num of clusters

    // custom comparator to sort array by a specific dimension
    private class DatasetComparator implements Comparator<int[]> {
        int ix; // index that we are sorting by

        // constructs comparator based on index ix
        public DatasetComparator(int ix) {
            this.ix = ix;
        }
        
        public int compare(int[] arr1, int[] arr2) {
            return Integer.compare(arr1[ix], arr2[ix]);
        }
        
    }

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException();
        }

        // num of transaction summaries (num of points)
        int n = input.length;

        if (labels.length != n || weights.length != n || n == 0) {
            throw new IllegalArgumentException();
        }

        // num of clusters (each like a point of k dim)
        k = input[0].length;

        // input that we can re-sort
        int[][] sortableInput = new int[n][k + 1];

        double totalWeight = 0;

        for (int i = 0; i < n; i++) {
            if (input[i].length == 0) throw new IllegalArgumentException();
            if (weights[i] < 0) throw new IllegalArgumentException();
            if (labels[i] != 0 && labels[i] != 1) {
                throw new IllegalArgumentException();
            }

            for (int j = 0; j < k; j++) {
                sortableInput[i][j] = input[i][j];
            }
            
            // kth column stores initial order
            sortableInput[i][k] = i;
            totalWeight += weights[i];
        }

        // calculates default weight as the middle weight
        double midWeight = totalWeight / 2;

        double maxScore = midWeight;

        // goes through and tries partitioning by each dimension
        for (int dim = 0; dim < k; dim++) {
            Arrays.sort(sortableInput, new DatasetComparator(dim));

            // calculates the default score for it the line was at the start
            double defaultScore = 0;
            for (int i = 0; i < n; i++) {
                int accIdx = sortableInput[i][k];
                if (labels[accIdx] == 1) defaultScore += weights[accIdx];
            }

            // iterates through each point and calculates a new score for if there
            // was a partition through that point at the current dimension
            double curScore = defaultScore;

            for (int i = 0; i < n; i++) {
                // new point setting partition
                int accIdx = sortableInput[i][k];
                
                // ADD COMMENTS
                if (labels[accIdx] == 0) curScore += weights[accIdx];
                else curScore -= weights[accIdx];

                if (i < n - 1 && sortableInput[i + 1][dim] == sortableInput[i][dim]) {
                    continue;
                }
                
                double newMax = midWeight + Math.abs(curScore - midWeight);
                if (newMax > maxScore) {
                    dp = dim;
                    vp = sortableInput[i][dim];
                    if (curScore < defaultScore) {
                        sp = 1;
                    } else {
                        sp = 0;
                    }
                    maxScore = newMax;
                }
            }
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) throw new IllegalArgumentException();
        if (sample.length != k) throw new IllegalArgumentException();

        if (sp == 1) {
            if (sample[dp] <= vp) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (sample[dp] <= vp) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dp;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return vp;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return sp;
    }

    // unit testing
    public static void main(String[] args) {
        
    }
}