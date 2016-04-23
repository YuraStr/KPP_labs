package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Contains size of window, table of cells,
 * some methods for work with board
 */
class Board extends GridPane {
  private static final String REPLAY_FILE_NAME = "Replay.txt";

  private static final int WONWINDOW_WIDTH = 250;
  private static final int WONWINDOW_HEIGHT = 120;
  private static final int FONT_SIZE = 14;
  private static final int BOTTOM_INDENT = -15;

  private int rowCount;
  private int columnCount;
  private int countOfBombs;
  private int countOfFlags;

  private ArrayList<Integer> bombs;
  private ArrayList<Integer> clicks;

  private boolean inGame;
  private boolean isReplay;

  private Cell cells[][];

  private Replay replay;

  /**
   * Initialize cells, add mines, start bot (if necessary)
   * @param rowCount      Count of rows
   * @param columnCount   Count of columns
   * @param countOfBombs  Count of bombs
   * @param useBot        true, if we want to use bot
   * @param isReplay      true, if we want to watch last replay
   */
  public void initCells(int rowCount, int columnCount, int countOfBombs,
                        boolean useBot, boolean isReplay) {
    replay = new Replay(this);
    this.isReplay = isReplay;
    this.rowCount = rowCount;
    this.columnCount = columnCount;
    this.countOfBombs = countOfBombs;
    this.countOfFlags = 0;

    bombs = new ArrayList<Integer>();
    clicks = new ArrayList<Integer>();

    inGame = true;

    cells = new Cell[rowCount][columnCount];

    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        cells[i][j] = new Cell();
        Cell cell = cells[i][j];
        add(cell, j, i);
      }
    }

    setAlignment(Pos.CENTER);

    if (!isReplay) {
      setMines();
      if (useBot) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new Bot(this));
        exec.shutdown();
      } else {
        setMouseEvents();
      }
    } else {
      try {
        InputStream inputStream = new FileInputStream(REPLAY_FILE_NAME);

        this.rowCount = inputStream.read();
        this.columnCount = inputStream.read();
        this.countOfBombs = inputStream.read();

        for (int i = 0; i < this.countOfBombs * 2; i++) {
          bombs.add(inputStream.read());
        }

        setMines();

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(replay);
        service.shutdown();

        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Set events for mouse
   */
  public void setMouseEvents() {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        Cell cell = cells[i][j];

        cell.setOnMouseClicked(event -> {
          if (inGame && !cell.isOpen()) {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
              clicks.add(1);
              clicks.add(GridPane.getRowIndex(cell));
              clicks.add(GridPane.getColumnIndex(cell));
              if (!cell.isFlag()) {
                cell.setFlag(true);
                countOfFlags++;
                return;
              }
              else {
                cell.setFlag(false);
                countOfFlags--;
                return;
              }
            }

            if (!cell.isFlag()) {
              clicks.add(0);
              clicks.add(GridPane.getRowIndex(cell));
              clicks.add(GridPane.getColumnIndex(cell));
              if (cell.isBomb()) {
                clearAllCells();
                cell.onBomb();
              }
              else
              if (cell.isEmpty()) {
                  openEmptyCells(GridPane.getRowIndex(cell), GridPane.getColumnIndex(cell));
                if (isWon()) {
                  openWonWindow();
                }
              }
              else
              if (!cell.isEmpty()) {
                cell.open();
                if (isWon()) {
                  openWonWindow();
                }
              }
            }
          }
        });
      }
    }
  }

  /**
   * Add mines on board
   */
  public void setMines() {
    if (!isReplay) {
      int n = countOfBombs;

      while (n > 0) {
        int i = (int) (Math.random() * rowCount);
        int j = (int) (Math.random() * columnCount);

        if (!cells[i][j].isBomb()) {
          cells[i][j].setBomb(true);
          n--;
          bombs.add(i);
          bombs.add(j);
        }
      }
    } else {
      for (int i = 0; i < bombs.size(); i += 2) {
        cells[bombs.get(i)][bombs.get(i+1)].setBomb(true);
      }
    }

    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        countOfNeighbors(i, j);
        cells[i][j].setText();
      }
    }
  }

  /**
   * Counts the number of neighbor mines
   * @param x cell coordinate in row
   * @param y cell coordinate in column
   */
  public void countOfNeighbors(int x, int y) {
    for (int i = -1; i <= 1; i++) {
      int newX = x + i;

      if (newX < 0 || newX >= rowCount) {
        continue;
      }

      for (int j = -1; j <= 1; j++) {
        int newY = y+j;

        if (newY < 0 || newY >= columnCount) {
          continue;
        }

        if (i == 0 && j == 0) {
          continue;
        }

        if(cells[newX][newY].isBomb()) {
          cells[x][y].addNeighbor();
        }
      }
    }
  }

  /**
   * Opens the cell if empty
   * @param x cell coordinate in row
   * @param y cell coordinate in column
   */
  public void openEmptyCells(int x, int y) {
    for (int i = -1; i <= 1; i++) {
      int newX = x + i;

      if (newX < 0 || newX >= rowCount) {
        continue;
      }

      for (int j = -1; j <= 1; j++) {
        int newY = y+j;

        if (newY < 0 || newY >= columnCount) {
          continue;
        }

        Cell cell = cells[newX][newY];
        if (cell.isEmpty() && !cell.isOpen()) {
          cell.open();
          openEmptyCells(newX, newY);
          openAroundCells(newX, newY);
        }
      }
    }
  }

  /**
   * Open cells around empty cell
   * @param x cell coordinate in row
   * @param y cell coordinate in column
   */
  public void openAroundCells(int x, int y) {
    for (int i = -1; i <= 1; i++) {
      int newX = x + i;

      if (newX < 0 || newX >= rowCount) {
        continue;
      }

      for (int j = -1; j <= 1; j++) {
        int newY = y + j;

        if (newY < 0 || newY >= columnCount) {
          continue;
        }

        if (i == 0 && j == 0) {
          continue;
        }

        Cell cell = cells[newX][newY];
        if (!cell.isBomb()) {
          cell.open();
        }
      }
    }
  }

  /**
   * Open all cells
   */
  public void clearAllCells() {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        cells[i][j].open();
      }
    }
    replay.record(rowCount, columnCount, countOfBombs, bombs, clicks);
  }

  /**
   * Checks if you won
   * @return true, if you won
   */
  public boolean isWon() {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        if (!cells[i][j].isBomb() && !cells[i][j].isOpen()) {
          return false;
        }
      }
    }
    replay.record(rowCount, columnCount, countOfBombs, bombs, clicks);
    return true;
  }

  /**
   * Open new window, when you won
   */
  public void openWonWindow() {
    inGame = false;

    BorderPane pane = new BorderPane();

    Text text = new Text("You have cleared the field!");
    text.setFont(Font.font("Arial", FontWeight.BLACK, FONT_SIZE));
    pane.setCenter(text);

    Button closeButton = new Button("Close");
    closeButton.setAlignment(Pos.CENTER);

    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER);
    hBox.getChildren().add(closeButton);
    hBox.setTranslateY(BOTTOM_INDENT);
    pane.setBottom(hBox);

    Scene wonScene = new Scene(pane, WONWINDOW_WIDTH, WONWINDOW_HEIGHT);

    Stage wonStage = new Stage();
    wonStage.setTitle("Congratulations!");
    wonStage.setScene(wonScene);
    wonStage.show();

    closeButton.setOnMouseClicked(event -> {
      wonStage.close();
    });
  }

  /**
   * @return count of remained bombs
   */
  public int getRemainedBombs() {
    return countOfBombs - countOfFlags;
  }

  /**
   * Use this method for open cells in bot
   * @param x cell coordinate in row
   * @param y cell coordinate in column
   */
  public void openCell(int x, int y) {
    Cell cell = cells[x][y];

    if (!cell.isFlag()) {
      clicks.add(0);
      clicks.add(x);
      clicks.add(y);
      if (cell.isBomb()) {
        clearAllCells();
        cell.onBomb();
        inGame = false;
      } else if (cell.isEmpty()) {
        openEmptyCells(x, y);
        if (isWon()) {
          openWonWindow();
          inGame = false;
        }
      } else if (!cell.isEmpty()) {
        cell.open();
        if (isWon()) {
          openWonWindow();
          inGame = false;
        }
      }
    }
  }

  /**
   * Set flag in cell with coordinates (x, y)
   * @param x cell coordinate in row
   * @param y cell coordinate in column
   */
  public void setFlag(int x, int y) {
    if (cells[x][y].isFlag()) {
      cells[x][y].setFlag(false);
    } else {
      cells[x][y].setFlag(true);
    }
  }

  /**
   * Checking, if cell is open
   * @param x cell coordinate in row
   * @param y cell coordinate in column
   * @return true, if cell is open
   */
  public boolean cellIsOpen(int x, int y) {
    return cells[x][y].isOpen();
  }

  /**
   * @return count of rows
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * @return count of columns
   */
  public int getColumnCount() {
    return columnCount;
  }

  /**
   * @return true, if you still in game
   */
  public boolean isInGame() {
    return inGame;
  }

  /**
   * @return count of bombs
   */
  public int getCountOfBombs() {
    return countOfBombs;
  }
}
