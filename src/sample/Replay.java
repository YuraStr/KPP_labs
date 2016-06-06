package sample;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Recording and playback of replays
 */
public class Replay implements Runnable{
  private String replayName;
  private String translatedReplayName;
  private Board board;

  private List<Integer> replayArray;
  private InputStream inputStream;
  private OutputStream outputStream;

  /**
   * Class constructor
   * @param board main board with cells
   */
  public Replay(Board board, String replayName) {
    if (!new File("Replays/").exists()) {
      new File("Replays/").mkdirs();
    }
    if (!new File("TranslatedReplays/").exists()) {
      new File("TranslatedReplays/").mkdirs();
    }
    this.replayName = replayName;
    if (replayName != null) {
      this.translatedReplayName = "TranslatedReplays/" + this.replayName.substring(8) + "_tr";
    }
    this.board = board;
  }

  /**
   * Playing replay
   */
  public void run() {
    try {
      inputStream = new FileInputStream(replayName);
      replayArray = new ArrayList<>();
      int[] array;

      for (int i = 0; i < board.getCountOfBombs() * 2 + 3; i++) {
        replayArray.add(inputStream.read());
      }

      while (true) {
        int mouse = inputStream.read();
        int x = inputStream.read();
        int y = inputStream.read();

        Thread.sleep(700);

        if (mouse == -1) {
          break;
        }

        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            if (mouse == 0) {
              board.openCell(x, y);
            } else {
              board.setFlag(x, y);
            }
            replayArray.add(mouse);
            replayArray.add(x);
            replayArray.add(y);
          }
        });
      }

      array = new int[replayArray.size()];
      int temp = replayArray.size();
      for (int i = 0; i < temp; i++) {
        array[i] = replayArray.get(i);
      }

      Translator.parse(array, translatedReplayName);
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
      outputStream = new FileOutputStream("Replays/" + new Date().toString());

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
