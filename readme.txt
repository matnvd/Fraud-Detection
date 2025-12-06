Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */

First, we created an EdgeWeightedGraph instance with the number of nodes being
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
      5          80             0.86875            0.262
      5          160            0.86875            0.405
      10         80             0.96               0.375
      20         80             0.96375            0.482
      40         80             0.96875            0.682
      80         80             0.95875            1.017
      60         80             0.95625            0.875
      60         160            0.9675             1.581
      50         320            0.9775             2.578
      60         320            0.9825             2.919
      60         640            0.98625            5.695
      60         1280           0.98625            11.407
      60         480            0.98625            4.324
      60         400            0.98125            3.672
      60         440            0.98               3.982
      60         460            0.98125            4.228
      60         470            0.98625            4.305
      60         465            0.98125            4.256
      60         467            0.9825             4.307
      60         469            0.9825             4.331
      

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */

1. We started off at the base values of k = 5 and T = 80. We would then run the test
to see the test accuracy of the model. We would then keep doubling T until the test
accuracy of the test plateaud. Once it did, We would double k and repeat the same
process with T. Once the test accuracy plateaud from both increasing k too, we would
find the exact value of T for the respective k at which it started plateauing at,
which in this case was T = 470 for k = 60.

2. A small value of T leads to low test accuracy because not enough iterations
done means that the algorithm hasn't had enough time to adjust the weights
properly to get the highest accuracy. Underfitting occurs because there would be
insufficient boosting.

3. Having a k too small means that you're not clustering enough and the clusters
are too course. Many different locations/classes are merged together and so there
is not enough signal to improve through boosting. Having a k too large will lead
to data that is really noisy and the boosting algorithm may accidentally amplify
that noise. This will lead to overfitting and higher variance.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
