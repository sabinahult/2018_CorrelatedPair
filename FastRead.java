import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
* Author: Sabina Hult
*/

public class FastRead {
  BufferedReader buffy;

  public FastRead(String filepath) throws FileNotFoundException {
    buffy = new BufferedReader(new FileReader(filepath));
  }

  public FastRead(InputStream i) throws IOException {
    buffy = new BufferedReader(new InputStreamReader(i));
  }

  public boolean reachedEnd() throws IOException {
    // if this is not the end of file, then don't advance the reader
    buffy.mark(1);

    if(buffy.read() == -1) return true;
    else buffy.reset();

    return false;
  }

  public int nextInt() throws IOException {
    int i = 0;

    int curChar = buffy.read();
    // skip non-integers
    while(curChar < 48 || curChar > 57) {
      curChar = buffy.read();
    }

    // read integer
    while(48 <= curChar && curChar <= 57) {
      buffy.mark(1);
      i = i*10 + (curChar - 48);
      curChar = buffy.read();
    }

    // take back last token read
    buffy.reset();
    return i;
  }

  public long nextLong() throws IOException {
    long i = 0;
    boolean negative = false;

    int curChar = buffy.read();

    // skip non-integers
    while(curChar < 48 || curChar > 57) {
      curChar = buffy.read();
      if(curChar == 45) negative = true;
    }

    // read integer
    while(48 <= curChar && curChar <= 57) {
      buffy.mark(1);
      i = i*10 + (curChar - 48);
      curChar = buffy.read();
    }

    // take back last token read
    buffy.reset();
    return negative ? (-1 * i) : i;
  }

  // advance past the new line token
  public void advanceToNextLine() throws IOException {
    int curChar = buffy.read();
    while(curChar != 10) curChar = buffy.read();
  }
}
