package sample;

import static java.lang.Thread.sleep;

/**
 * Create bot in another thread
 */
public class Bot implements Runnable {
  private static final int SLEEP_TIME = 700;

  private Board board = new Board();

  /**
   * Class constructor
   * @param board main board with mines
   */
  public Bot(Board board) {
    this.board = board;
  }

  /**
   * Overridden method from interface "Runnable"
   */
  public void run() {
    while (true) {
      if (board.isInGame()) {
        int i = (int) (Math.random() * board.getRowCount());
        int j = (int) (Math.random() * board.getColumnCount());
        if (!board.cellIsOpen(i, j)) {
          board.openCell(i, j);
          try {
            sleep(SLEEP_TIME);
          } catch (InterruptedException exc) {
            System.out.println(exc.getMessage());
          }
        }
      } else {
        break;
      }
    }
  }
}