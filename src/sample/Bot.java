package sample;


import static java.lang.Thread.sleep;

/**
 * Created by yura_str on 3/31/16.
 */

public class Bot implements Runnable {
  private static final int SLEEP_TIME = 700;

  private Thread thread;
  private Board board = new Board();

  public Bot(Board board) {
    thread = new Thread(this);
    this.board = board;
    thread.start();
  }

  public void run() {
    while (true) {
      if (board.isInGame()) {
        int i = (int) (Math.random() * board.getRowCount());
        int j = (int) (Math.random() * board.getColumnCount());
        synchronized (board) {
          board.openCell(i, j);
          try {
            board.wait();
            sleep(SLEEP_TIME);
          } catch (InterruptedException exc) {
            System.out.println(exc.getMessage());
          }
        }
      }
      else {
        break;
      }
    }
  }
}