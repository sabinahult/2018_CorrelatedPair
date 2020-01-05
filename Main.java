import java.util.*;
import java.io.IOException;

/**
* Author: Sabina Hult
*/

public class Main {
  public static void main(String[] args) {
    List<DataPoint> datapoints = new ArrayList<>();

    try {
      //long start = System.currentTimeMillis();
      FastRead f = new FastRead(System.in);
      int n = f.nextInt();
      f.advanceToNextLine();

      for(int id = 0; id < n; id++) {
        long[] vals = new long[] {f.nextLong(), f.nextLong(), f.nextLong(), f.nextLong()};
        datapoints.add(new DataPoint(id, vals));
      }
      //System.out.println("Done reading in and creating data points at " + (System.currentTimeMillis()-start) + " ms");

      int[] corrPair = CorrPair.find(datapoints);
      //System.out.println("Done finding correlated pair at " + (System.currentTimeMillis()-start) + " ms");

      for(int i : corrPair) System.out.print(i + " ");
      System.out.println();
    } catch (IOException e) {
      System.out.println("IOException");
    }
  }
}
