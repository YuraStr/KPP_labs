package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import scala.Int;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu window and main window
 */
public class Interface {
  private static final int SCENE_HEIGHT = 300;
  private static final int SCENE_WIDTH = 400;
  private static final int BUTTON_HEIGHT = 10;
  private static final int BUTTON_WIDTH = 100;
  private static final int REPLAY_BUTTON_HEIGHT = 10;
  private static final int REPLAY_BUTTON_WIDTH = 150;

  private static final int TABLE_HEIGHT = 450;
  private static final int TABLE_WIDTH = 480;
  private static final int DATE_COLUMN_WIDTH = 180;
  private static final int BOMBS_COLUMN_WIDTH = 100;
  private static final int ROW_COLUMN_WIDTH = 100;
  private static final int COLUMN_COLUMN_WIDTH = 100;

  private static final int REPLAYS_SCENE_WIDTH = 500;
  private static final int REPLAYS_SCENE_HEIGHT = 550;

  private static final int STATISTICS_SCENE_WIDTH = 250;
  private static final int STATISTICS_SCENE_HEIGHT = 200;

  private static final int FONT_SIZE_14 = 14;
  private static final int FONT_SIZE_20 = 20;

  private static final int CELL_WIDTH = 20;
  private static final int CELL_HEIGHT = 20;

  private static final int BEGINNER_ROWS = 9;
  private static final int BEGINNER_COLUMNS = 9;
  private static final int BEGINNER_BOMBS = 9;

  private static final int ADVANCED_ROWS = 16;
  private static final int ADVANCED_COLUMNS = 16;
  private static final int ADVANCED_BOMBS = 40;

  private static final int PRO_ROWS = 16;
  private static final int PRO_COLUMNS = 30;
  private static final int PRO_BOMBS = 99;

  private static final int SPACING_1 = 15;
  private static final int SPACING_2 = 20;
  private static final int TOP_INDENT = 10;
  private static final int BOTTOM_INDENT = -15;
  private static final int OFFSET = 100;

  private static final String REPLAY_FOLDER = "Replays/";

  private Stage stage;
  private boolean inReplay = false;
  private String replayFileName = null;

  /**
   * Class constructor
   * @param stage where add window objects
   */
  public Interface(Stage stage) {
        this.stage = stage;
    }

  /**
   * Create menu window
   */
  public void Menu() {
    BorderPane borderPane = new BorderPane();

    HBox hBoxTop = new HBox();
    Text welcomeText = new Text("Welcome to the game \"Minesweeper\"!");
    welcomeText.setFont(Font.font("Arial", FontWeight.NORMAL, FONT_SIZE_14));
    hBoxTop.setAlignment(Pos.CENTER);
    hBoxTop.getChildren().add(welcomeText);
    hBoxTop.setTranslateY(TOP_INDENT);
    borderPane.setTop(hBoxTop);

    Text difficultText = new Text("Select difficulty level");
    difficultText.setFont(Font.font(FONT_SIZE_14));

    ToggleGroup toggleGroup = new ToggleGroup();

    RadioButton radioButtonBeginner = new RadioButton("Beginner");
    radioButtonBeginner.setSelected(true);
    radioButtonBeginner.setToggleGroup(toggleGroup);

    RadioButton radioButtonAdvanced = new RadioButton("Amateur");
    radioButtonAdvanced.setToggleGroup(toggleGroup);

    RadioButton radioButtonPro = new RadioButton("Professional");
    radioButtonPro.setToggleGroup(toggleGroup);

    CheckBox useBotCheckBox = new CheckBox("Use bot");
    useBotCheckBox.setAlignment(Pos.CENTER);

    Button startButton = new Button("Start");
    startButton.setPrefWidth(BUTTON_WIDTH);
    startButton.setPrefHeight(BUTTON_HEIGHT);
    startButton.setAlignment(Pos.CENTER);

    VBox vBox = new VBox();
    vBox.getChildren().add(difficultText);
    vBox.getChildren().addAll(radioButtonBeginner, radioButtonAdvanced, radioButtonPro,
                              useBotCheckBox, startButton);
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(SPACING_1);
    borderPane.setCenter(vBox);

    VBox vBoxBottom = new VBox();
    Button replayButton = new Button("Watch last replay");
    replayButton.setPrefWidth(REPLAY_BUTTON_WIDTH);
    replayButton.setPrefHeight(REPLAY_BUTTON_HEIGHT);
    replayButton.setAlignment(Pos.CENTER);

    Button statisticsButton = new Button("Statistics");
    statisticsButton.setPrefWidth(BUTTON_WIDTH);
    statisticsButton.setPrefHeight(BUTTON_HEIGHT);
    statisticsButton.setAlignment(Pos.CENTER);

    vBoxBottom.setAlignment(Pos.CENTER);
    vBoxBottom.setSpacing(SPACING_2);
    vBoxBottom.getChildren().addAll(statisticsButton, replayButton);
    vBoxBottom.setTranslateY(BOTTOM_INDENT);
    borderPane.setBottom(vBoxBottom);

    Scene menuScene = new Scene(borderPane, SCENE_HEIGHT, SCENE_WIDTH);

    stage.setTitle("Minesweeper");
    stage.setScene(menuScene);
    stage.show();

    startButton.setOnMouseClicked(event -> {
      if (!useBotCheckBox.isSelected()) {
        if (radioButtonBeginner.isSelected()) {
          Game(BEGINNER_ROWS, BEGINNER_COLUMNS, BEGINNER_BOMBS, false);
        } else if (radioButtonAdvanced.isSelected()) {
          Game(ADVANCED_ROWS, ADVANCED_COLUMNS, ADVANCED_BOMBS, false);
        } else if (radioButtonPro.isSelected()) {
          Game(PRO_ROWS, PRO_COLUMNS, PRO_BOMBS, false);
        }
      }
      else {
        if (radioButtonBeginner.isSelected()) {
          Game(BEGINNER_ROWS, BEGINNER_COLUMNS, BEGINNER_BOMBS, true);
        } else if (radioButtonAdvanced.isSelected()) {
          Game(ADVANCED_ROWS, ADVANCED_COLUMNS, ADVANCED_BOMBS, true);
        } else if (radioButtonPro.isSelected()) {
          Game(PRO_ROWS, PRO_COLUMNS, PRO_BOMBS, true);
        }
      }
    });

    replayButton.setOnMouseClicked(event -> {
      Replays();
    });

    statisticsButton.setOnMouseClicked(event -> {
      StatisticWindow();
    });
  }

  /**
   * Create main window
   * @param rowCount      count of rows
   * @param columnCount   count of columns
   * @param countOfMines  count of mines
   * @param useBot        true, if we want to use bot
   */
  public void Game(int rowCount, int columnCount, int countOfMines, boolean useBot) {
    BorderPane root = new BorderPane();

    Board board = new Board();
    board.setReplayFileName(replayFileName);
    board.initCells(rowCount, columnCount, countOfMines, useBot, inReplay);
    root.setCenter(board);

    Button restartBtn = new Button("Restart");
    restartBtn.setAlignment(Pos.CENTER);

    Text remainedBombs = new Text();
    remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));

    board.setOnMouseClicked(event -> {
      remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));
    });

    restartBtn.setOnMouseClicked(event -> {
      board.initCells(rowCount, columnCount, countOfMines, useBot, inReplay);
      remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));
    });

    HBox hBoxTop = new HBox();
    hBoxTop.setAlignment(Pos.CENTER);
    hBoxTop.getChildren().add(remainedBombs);
    hBoxTop.setTranslateY(TOP_INDENT);

    HBox hBoxBottom = new HBox();
    hBoxBottom.setAlignment(Pos.CENTER);
    hBoxBottom.getChildren().add(restartBtn);
    hBoxBottom.setTranslateY(BOTTOM_INDENT);

    root.setTop(hBoxTop);
    root.setBottom(hBoxBottom);

    Scene scene = new Scene(root, columnCount * CELL_WIDTH + OFFSET, rowCount * CELL_HEIGHT + OFFSET);
    stage.setScene(scene);
    stage.show();
  }

  public void Replays() {
    Pane root = new Pane();

    Label label = new Label("Replays");
    label.setFont(Font.font(FONT_SIZE_20));

    TableView<FileData> tableView = new TableView<>();
    tableView.setPrefHeight(TABLE_HEIGHT);
    tableView.setPrefWidth(TABLE_WIDTH);

    TableColumn<FileData, String> dateColumn = new TableColumn<>("Date and time");
    TableColumn<FileData, Integer> bombsColumn = new TableColumn<>("Bombs");
    TableColumn<FileData, Integer> rowCountColumn = new TableColumn<>("Rows");
    TableColumn<FileData, Integer> columnCountColumn = new TableColumn<>("Columns");

    dateColumn.setPrefWidth(DATE_COLUMN_WIDTH);
    bombsColumn.setPrefWidth(BOMBS_COLUMN_WIDTH);
    rowCountColumn.setPrefWidth(ROW_COLUMN_WIDTH);
    columnCountColumn.setPrefWidth(COLUMN_COLUMN_WIDTH);

    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    bombsColumn.setCellValueFactory(new PropertyValueFactory<>("bombs"));
    rowCountColumn.setCellValueFactory(new PropertyValueFactory<>("rows"));
    columnCountColumn.setCellValueFactory(new PropertyValueFactory<>("columns"));

    ObservableList<FileData> observableList = FXCollections.observableList(getListOfReplays());
    tableView.setItems(observableList);
    tableView.getColumns().addAll(dateColumn, bombsColumn, rowCountColumn, columnCountColumn);

    Button playButton = new Button("Play");
    playButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    playButton.setDisable(true);

    playButton.setOnMouseClicked(event -> {
      FileData fileData = tableView.getSelectionModel().getSelectedItem();
      inReplay = true;
      replayFileName = REPLAY_FOLDER + fileData.getDate();
      Game(fileData.getRows(), fileData.getColumns(), fileData.getBombs(), false);
    });

    tableView.setOnMouseClicked(event -> {
      playButton.setDisable(false);
    });

    VBox vBox = new VBox();
    vBox.setSpacing(SPACING_1);
    vBox.setPadding(new Insets(10, 0, 0, 10));
    vBox.setAlignment(Pos.CENTER);
    vBox.getChildren().addAll(label, tableView, playButton);

    root.getChildren().add(vBox);

    Scene scene = new Scene(root, REPLAYS_SCENE_WIDTH, REPLAYS_SCENE_HEIGHT);
    stage.setScene(scene);
    stage.show();
  }

  public List<FileData> getListOfReplays() {
    File file = new File(REPLAY_FOLDER);
    String[] files = file.list();
    List<FileData> data = new ArrayList<>();

    int rowCount;
    int columnCount;
    int countOfBombs;

    for (String fileName : files) {
      try {
        InputStream inputStream = new FileInputStream(REPLAY_FOLDER + fileName);

        rowCount = inputStream.read();
        columnCount = inputStream.read();
        countOfBombs = inputStream.read();

        data.add(new FileData(fileName, countOfBombs, rowCount, columnCount));

        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    sortItems(data);

    return data;
  }

  public void sortItems(List<FileData> data) {
    long start, end;

    int[] array = new int[data.size()];
    for (int j = 0; j < data.size(); j++) {
      array[j] = data.get(j).getBombs();
    }
    int[] array2 = array.clone();

    JavaSort javaSort = new JavaSort();
    start = System.nanoTime();
    javaSort.sort(array);
    end = System.nanoTime();
    System.out.println("Java:  " + (end - start));

    ScalaFunctions scalaSort = new ScalaFunctions();
    start = System.nanoTime();
    scalaSort.qsort(array2, 0, array2.length - 1);
    end = System.nanoTime();
    System.out.println("Scala: " + (end - start));
  }

  public void StatisticWindow() {
    BorderPane root = new BorderPane();
    Statistics statistics = new Statistics();

    ScalaFunctions scalaFunctions = new ScalaFunctions();

    Label rightClicksLabel = new Label("Count of right clicks: " +
      scalaFunctions.getRightClicks(statistics.getClicksArray()));
    Label leftClicksLabel  = new Label("Count of left clicks: " +
      scalaFunctions.getLeftClicks(statistics.getClicksArray()));
    Label rowsLabel = new Label("Count of rows: " +
      statistics.getCountOfRows());
    Label columnsLabel = new Label("Count of columns: " +
      statistics.getCountOfColumns());
    Label bombsLabel = new Label("Count of bombs: " +
      statistics.getCountOfBombs());

    VBox vBox = new VBox();
    vBox.setAlignment(Pos.CENTER);
    vBox.getChildren().addAll(rightClicksLabel, leftClicksLabel, rowsLabel,
      columnsLabel, bombsLabel);

    Button backButton = new Button("Back");
    backButton.setAlignment(Pos.CENTER);
    backButton.setPrefWidth(BUTTON_WIDTH);
    backButton.setPrefHeight(BUTTON_HEIGHT);

    backButton.setOnMouseClicked(event -> {
      Menu();
    });

    VBox vBoxBot = new VBox();
    vBoxBot.setAlignment(Pos.CENTER);
    vBoxBot.setTranslateY(BOTTOM_INDENT);
    vBoxBot.getChildren().add(backButton);

    root.setCenter(vBox);
    root.setBottom(vBoxBot);

    Scene scene = new Scene(root, STATISTICS_SCENE_WIDTH, STATISTICS_SCENE_HEIGHT);
    stage.setScene(scene);
    stage.show();
  }
}
