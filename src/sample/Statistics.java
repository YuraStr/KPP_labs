package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura_str on 6/1/16.
 */
public class Statistics {
  private int[] clicksArray;
  private int countOfBombs;
  private int countOfRows;
  private int countOfColumns;

  public Statistics() {
    File folder = new File("./Replays/");
    File[] listFiles = folder.listFiles();
    List<Integer> clicks = new ArrayList<>();

    for (int i = 0; i < listFiles.length; i++) {
      try {
        InputStream inputStream = new FileInputStream(listFiles[i].getAbsolutePath());

        int temp = inputStream.read();
        countOfRows += temp;
        temp = inputStream.read();
        countOfColumns += temp;
        temp = inputStream.read();
        countOfBombs += temp;

        for (int j = 0; j < temp * 2; j++) {
          inputStream.read();
        }

        int click;
        do {
          click = inputStream.read();
          if (click != -1) {
            clicks.add(click);
          } else {
            break;
          }
          inputStream.read();
          inputStream.read();
        } while (true);

        inputStream.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    clicksArray = new int[clicks.size()];
    for (int i = 0; i < clicks.size(); i++) {
      clicksArray[i] = clicks.get(i);
    }
  }

  public int[] getClicksArray() {
    return clicksArray;
  }

  public int getCountOfBombs() { return countOfBombs; }

  public int getCountOfRows() { return countOfRows; }

  public int getCountOfColumns() { return countOfColumns; }
}
