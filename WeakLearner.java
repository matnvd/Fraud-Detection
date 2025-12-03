import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.Merge;

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

        int k = input[0].length+1;

        int[][] sortableInput = new int[input.length][k];

        for(int i = 0; i < input.length; i++) {
            for(int j = 0; j < input[0].length; j++) {
                sortableInput[i][j] = input[i][j];
            }
            sortableInput[i][input[0].length] = i;
        }

        double maxScore = 0;

        for(int dim = 0; dim < input.length; dim++) {
            Arrays.sort(sortableInput, new DatasetComparator(dim));

            int tempVp = -1;
            int defaultScore = 0;
            for(int i = 0; i < sortableInput.length; i++) {
                if(labels[sortableInput[i][k]] == 1) defaultScore += weights[sortableInput[i][k]];
            }

            int curScore = defaultScore;

            for(int i = 0; i < sortableInput.length; i++) {
                if(labels[sortableInput[i][k]] == 0) curScore += weights[sortableInput[i][k]];
                else curScore -= weights[sortableInput[i][k]];
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
        if(sp==1) return sample[dp]<=vp ? 1 : 0;
        else return sample[dp]<=vp ? 0 : 1;
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