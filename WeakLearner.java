import java.util.Arrays;
import java.util.Comparator;

public class WeakLearner {

    private int sp, vp, dp;

    private class DatasetComparator implements Comparator<int[]> {
        int ix;

        public DatasetComparator(int ix) {
            this.ix = ix;
        }

        public int compare(int[] arr1, int[] arr2) {
            return Integer.compare(arr1[ix], arr2[ix]);
        }
        
    }

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if(input == null || weights == null || labels == null) throw new IllegalArgumentException();

        // num of transaction summaries (num of points)
        int n = input.length;
        // num of clusters (each like a point of k dim)
        int k = input[0].length;

        int[][] sortableInput = new int[n][k + 1];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < k; j++) {
                sortableInput[i][j] = input[i][j];
            }
            
            // kth column stores initial order
            sortableInput[i][k] = i;
        }

        double maxScore = 0;

        for(int dim = 0; dim < n; dim++) {
            Arrays.sort(sortableInput, new DatasetComparator(dim));

            // calculates the default score for it the line was at the top and 
            int defaultScore = 0;
            for(int i = 0; i < n; i++) {
                int accIdx = sortableInput[i][k];
                if(labels[accIdx] == 1) defaultScore += weights[accIdx];
            }

            // iterates through each point and calculates a new score for if there
            // was a partition through that point at the current dimension
            int curScore = defaultScore;

            for(int i = 0; i < n; i++) {
                // new point setting partition
                int accIdx = sortableInput[i][k];
                
                // 
                if(labels[accIdx] == 0) curScore += weights[accIdx];
                else curScore -= weights[accIdx];
                
                int newMax = defaultScore + Math.abs(curScore - defaultScore);
                if(newMax > maxScore) {
                    dp = dim;
                    vp = sortableInput[i][dim];
                    sp = curScore < defaultScore ? 1 : 0;
                }
            }
        }
    }

    // return the prediction of the learner for a new sample  
    public int predict(int[] sample) {
        if (sample == null) throw new IllegalArgumentException();

        if(sp == 1) {
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