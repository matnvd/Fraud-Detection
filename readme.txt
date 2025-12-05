Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */

First, we created an EdgeWeightedGraph instance with the number of nodes being
the number of locations in the dataset. We then iterated through a nested loop
to add edges between each unique pair of locations. We used the Point2D distanceTo
method to compute the weights for each edge. We then initialized a KruskalMST
class with the given EdgeWeightedGraph and moved the list of edges into an
Edge[] from an Iterable<Edge>. This allows us to sort the edges in ascending
order by weight and use the first n-k edges in this array to create a new 
EdgeWeightedGraph between the items only containing these n-k edges in the MST.
This was then passed to a connected components instance (CC), which is added
to an instance variable and used to access cluster IDs.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */

After checking for all corner cases (length matching and null) we copy the
input 2d array to a new array called sortableInput, which has an extra row
at the bottom. This row holds the original index of each input. This is needed
because inputs get mixed up when we sort them by a specific coordinate, so we 
need a way to keep track of the original index to access the corresponding weight
and label for each item. We also compute the sum of all weights divided by 2. This
is used to compute the maxScore of a partition for both signs in a quick fashion.
Next, we iterate through each dimension and sort the array of points 
(the copied one) in ascending order by the coordinate corresponding to the current 
dimension in the loop. After this, we evalute a defaultScore variable, which is 
what the maxWeight would be if the model predicted 1 for all points. We then go
through the points in ascending order by coordinate (linear b/c already sorted)
and add the weight of the new point if it becomes correct when we move the 
partition to that point (it crosses to the other side, prediction 0). If the
current score at the partition is below the middle score, we calculate the
sign-flipped score by computing the middle score + the absolute value of the 
current score - the middle score. If this is greater than any previous maxScore,
we update the max sp, vp, and dp and the maxScore. We iterate through each dimension
and each point in order to find the max in this fashion.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
