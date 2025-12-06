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
        
        // compares with specific index
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

        // copy of original input with extra column to keep original index
        // preserves index after in place sort Arrays.sort
        int[][] sortableInput = new int[n][k + 1];

        // calculates sum of all weights
        double totalWeight = 0;

        for (int i = 0; i < n; i++) {
            if (input[i].length == 0) throw new IllegalArgumentException();
            if (weights[i] < 0) throw new IllegalArgumentException();
            if (labels[i] != 0 && labels[i] != 1) {
                throw new IllegalArgumentException();
            }

            // populate sortable input
            for (int j = 0; j < k; j++) {
                sortableInput[i][j] = input[i][j];
            }
            
            // kth column stores initial order
            sortableInput[i][k] = i;
            totalWeight += weights[i];
        }

        // holds the 50% weight value, used to compute opposite sign value
        // score - midWeight returns max score for opposite sign
        double midWeight = totalWeight / 2;

        double maxScore = midWeight;

        // iterate through each dimension
        for (int dim = 0; dim < k; dim++) {
            // sort by the current dimension
            Arrays.sort(sortableInput, new DatasetComparator(dim));

            // calculates the default score (assume 1 predicted for all items)
            double defaultScore = 0;
            for (int i = 0; i < n; i++) {
                int accIdx = sortableInput[i][k];
                if (labels[accIdx] == 1) defaultScore += weights[accIdx];
            }

            // initial score to update by adding/removing weight from each point
            double curScore = defaultScore;
            
            // iterate through each point in ascending order by dimension
            for (int i = 0; i < n; i++) {
                // stores original index of the data point
                int accIdx = sortableInput[i][k];
                
                // now, we move the partition up to this point
                // since one point gets moved to the other side, we calculate new
                // score by either adding or subracting from the previous score

                // current point switch to lower side, if value matches, add weight
                if (labels[accIdx] == 0) curScore += weights[accIdx];
                else curScore -= weights[accIdx]; // if point is now wrong, subtract

                // if next point has same value, do not evaluate score
                // this is needed bc not all the points are shifted with duplicates
                if (i < n - 1 && sortableInput[i + 1][dim] == sortableInput[i][dim]) {
                    continue;
                }
                
                // determine distance from 50% randomness (determines value)
                double newMax = midWeight + Math.abs(curScore - midWeight);
                // update to new max if is greater
                if (newMax > maxScore) {
                    dp = dim;
                    vp = sortableInput[i][dim];
                    // if it is below 50%, switch sign to increase accuracy
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