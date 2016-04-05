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

import java.util.ArrayList;

import static java.lang.Thread.sleep;

class Board extends GridPane {
  private static final int WONWINDOW_WIDTH = 250;
  private static final int WONWINDOW_HEIGHT = 120;
  private static final int FONT_SIZE = 14;
  private static final int BOTTOM_INDENT = -15;


  private int rowCount;
  private int columnCount;
  private int countOfBombs;
  private int countOfFlags;

  private boolean inGame;

  private Bot bot;

  private Cell cells[][];

  public void initCells(int rowCount, int columnCount, int countOfBombs, boolean useBot) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;
    this.countOfBombs = countOfBombs;
    this.countOfFlags = 0;

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
    setMines();

    if (useBot) {
      bot = new Bot(this);
    }
    else {
      setMouseEvents();
    }
  }

  public void setMouseEvents() {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        Cell cell = cells[i][j];

        cell.setOnMouseClicked(event -> {
          if (inGame && !cell.isOpen()) {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
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

  public void setMines() {
    int n = countOfBombs;

    while (n > 0) {
      int i = (int) (Math.random() * rowCount);
      int j = (int) (Math.random() * columnCount);

      if (!cells[i][j].isBomb()) {
        cells[i][j].setBomb(true);
        n--;
      }
    }

    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        countOfNeighbors(i, j);
        cells[i][j].setText();
      }
    }
  }

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

  public void clearAllCells() {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        cells[i][j].open();
      }
    }
  }

  public boolean isWon() {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        if (!cells[i][j].isBomb() && !cells[i][j].isOpen()) {
          return false;
        }
      }
    }
    return true;
  }

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

  public int getRemainedBombs() {
    return countOfBombs - countOfFlags;
  }

  public void openCell(int x, int y) {
    synchronized (this) {
      Cell cell = cells[x][y];

      if (inGame && !cell.isOpen()) {
        if (!cell.isFlag()) {
          cell.open();
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
      this.notify();
    }
  }

  public int getRowCount() {
    return rowCount;
  }

  public int getColumnCount() {
    return columnCount;
  }

  public boolean isInGame() {
    return inGame;
  }
}
