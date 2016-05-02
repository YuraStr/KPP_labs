package sample;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Recording and playback of replays
 */
public class Replay implements Runnable{
  private String replayName;

  private Board board;

  /**
   * Class constructor
   * @param board main board with cells
   */
  public Replay(Board board, String replayName) {
    if (!new File("Replays/").exists()) {
      new File("Replays/").mkdirs();
    }
    this.replayName = replayName;
    this.board = board;
  }

  /**
   * Playing replay
   */
  public void run() {
    try {
      InputStream inputStream = new FileInputStream(replayName);

      for (int i = 0; i < board.getCountOfBombs() * 2 + 3; i++) {
        inputStream.read();
      }

      while (true) {
        int mouse = inputStream.read();
        int x = inputStream.read();
        int y = inputStream.read();

        Thread.sleep(700);

        if (mouse == -1) break;

        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            if (mouse == 0) {
              board.openCell(x, y);
            } else {
              board.setFlag(x, y);
            }
          }
        });

        System.out.println(mouse + " " + x + " " + y);
      }

      inputStream.close();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Recording last actions in file
   * @param countOfRows    count of rows at board
   * @param countOfColumns count of columns at board
   * @param countOfBombs   count if bombs at board
   * @param bombs          array with bombs coordinates
   * @param clicks         array with player's clicks on board
   */
  public void record(int countOfRows, int countOfColumns, int countOfBombs,
                     ArrayList<Integer> bombs, ArrayList<Integer> clicks) {
    try {
      OutputStream outputStream = new FileOutputStream("Replays/" + new Date().toString());

      outputStream.write(countOfRows);
      outputStream.write(countOfColumns);
      outputStream.write(countOfBombs);

      for (Integer bomb : bombs) {
        outputStream.write(bomb);
      }

      for (Integer click : clicks) {
        outputStream.write(click);
      }

      outputStream.flush();
      outputStream.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
