public class WeakLearner {

    // train the weak learner  
    public WeakLearner(int[][] input, double[] weights, int[] labels)

    // return the prediction of the learner for a new sample  
    public int predict(int[] sample)

    // return the dimension the learner uses to separate the data  
    public int dimensionPredictor()

    // return the value the learner uses to separate the data 
    public int valuePredictor()

    // return the sign the learner uses to separate the data 
    public int signPredictor()

    // unit testing
    public static void main(String[] args)
}