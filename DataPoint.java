/**
* Author: Sabina Hult
*/

public class DataPoint {
    private long[] vals;
    private int id;

    public DataPoint(int id, long[] vals) {
        this.id = id;
        this.vals = vals;
    }

    public long[] getVals() {
        return vals;
    }

    public int getID() {
        return id;
    }
}
