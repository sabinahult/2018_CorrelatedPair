import java.util.*;

/**
* Author: Sabina Hult
*/

// my proposed solution for finding a correlated pair of 256-bit vectors
// when using several minHash functions
public class CorrPair {
  private static List<Integer> firstHashVector;
  private static List<Integer> secondHashVector;
  private static List<Integer> thirdHashVector;

  private static Map<Integer, List<DataPoint>> buckets;
  private static final int THRESHOLD = 70;

  public static int[] find(List<DataPoint> input) {
    int[] pair = new int[] {-1, -1};
    while(pair[0] == -1) {
      pair = searchForPair(input);
    }

    return pair;
  }

  // set vectors, distribute points and return result of search in buckets
  private static int[] searchForPair(List<DataPoint> input) {
    firstHashVector = setHashVector();
    secondHashVector = setHashVector();
    thirdHashVector = setHashVector();

    buckets = distributePoints(input);

    return searchBuckets();
  }

  // create a vector with numbers 1-256 in random order
  private static List<Integer> setHashVector() {
    List<Integer> vector = new ArrayList<>();
    for(int i = 0; i < 256; i++) vector.add(i + 1);

    Collections.shuffle(vector);
    return vector;
  }

  // distribute points using additional minHash the bigger the input is
  private static Map<Integer, List<DataPoint>> distributePoints(List<DataPoint> input) {
    Map<Integer, List<DataPoint>> result = new HashMap<>();

    for(DataPoint dp : input) {
      int key;

      if(input.size() < 100000) key = createKey(dp, 1);
      else if(input.size() < 500000) key = createKey(dp, 2);
      else key = createKey(dp, 3);

      if(!result.containsKey(key)) result.put(key, new ArrayList<>());
      result.get(key).add(dp);
    }

    return result;
  }

  //implement minHash function with a hash vector containing numbers 1-256 in random order
  private static int minHash(DataPoint dp, List<Integer> vector) {

    BitSet bs = BitSet.valueOf(dp.getVals());
    for(int i = 0; i < vector.size(); i++) {
      if(bs.get(vector.get(i))) return i;
    }

    // hopefully this never happens :|
    return -1;
  }

  // create key with given number of minHash functions
  private static int createKey(DataPoint dp, int numOfMinHash) {
    if(numOfMinHash == 1) {
      return minHash(dp, firstHashVector);

    } else if(numOfMinHash == 2) {
      int[] keyVector = new int[2];
      keyVector[0] = minHash(dp, firstHashVector);
      keyVector[1] = minHash(dp, secondHashVector);
      return (int) ((keyVector[0] * 10e1) + (keyVector[1] * 10e0));

    } else {
      int[] keyVector = new int[3];
      keyVector[0] = minHash(dp, firstHashVector);
      keyVector[1] = minHash(dp, secondHashVector);
      keyVector[2] = minHash(dp, thirdHashVector);
      return (int) ((keyVector[0] * 1e2) + (keyVector[1] * 1e1) + (keyVector[2] * 1e0));
    }
  }

  // search buckets for correlated pair
  private static int[] searchBuckets() {
    int[] pair;
    for(Integer k : buckets.keySet()) {
      List<DataPoint> points = buckets.get(k);

      if(points.size() > 1) {
        pair = compareAll(points);
        if(pair != null) return pair;
      }
    }

    return new int[] {-1, -1};
  }

  // brute force compare all datapoints to every other datapoint
  private static int[] compareAll(List<DataPoint> data) {
    int[] pair = new int[2];

    for(int i = 0; i < data.size(); i++) {
      long[] valsI = data.get(i).getVals();

      for(int j = i + 1; j < data.size(); j++) {
        long[] valsJ = data.get(j).getVals();

        int commonOnes = 0;
        for(int k = 0; k < 4; k++) {
          commonOnes += Long.bitCount(valsI[k] & valsJ[k]);
        }

        if(commonOnes >= THRESHOLD) {
          pair[0] = data.get(i).getID();
          pair[1] = data.get(j).getID();
          return pair;
        }
      }
    }

    return null;
  }
}
