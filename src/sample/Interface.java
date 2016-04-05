package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Interface {
  private static final int SCENE_HEIGHT = 300;
  private static final int SCENE_WIDTH = 300;
  private static final int BUTTON_HEIGHT = 10;
  private static final int BUTTON_WIDTH = 100;

  private static final int FONT_SIZE = 14;

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

  private static final int SPACING_1 = 10;
  private static final int SPACING_2 = 40;
  private static final int TOP_INDENT = 10;
  private static final int BOTTOM_INDENT = -15;
  private static final int OFFSET = 100;

  private Stage stage;

  public Interface(Stage stage) {
        this.stage = stage;
    }

  public void Menu() {
    BorderPane borderPane = new BorderPane();

    HBox hBoxTop = new HBox();
    Text welcomeText = new Text("Welcome to the game \"Minesweeper\"!");
    welcomeText.setFont(Font.font("Arial", FontWeight.NORMAL, FONT_SIZE));
    hBoxTop.setAlignment(Pos.CENTER);
    hBoxTop.getChildren().add(welcomeText);
    hBoxTop.setTranslateY(TOP_INDENT);
    borderPane.setTop(hBoxTop);

    Text difficultText = new Text("Select difficulty level");
    difficultText.setFont(Font.font(FONT_SIZE));

    ToggleGroup toggleGroup = new ToggleGroup();

    RadioButton radioButtonBeginner = new RadioButton("Beginner");
    radioButtonBeginner.setSelected(true);
    radioButtonBeginner.setToggleGroup(toggleGroup);

    RadioButton radioButtonAdvanced = new RadioButton("Amateur");
    radioButtonAdvanced.setToggleGroup(toggleGroup);

    RadioButton radioButtonPro = new RadioButton("Professional");
    radioButtonPro.setToggleGroup(toggleGroup);

    VBox vBox = new VBox();
    vBox.getChildren().add(difficultText);
    vBox.getChildren().addAll(radioButtonBeginner, radioButtonAdvanced, radioButtonPro);
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(SPACING_1);
    borderPane.setCenter(vBox);

    VBox vBoxBottom = new VBox();
    Button startButton = new Button("Start");
    startButton.setPrefWidth(BUTTON_WIDTH);
    startButton.setPrefHeight(BUTTON_HEIGHT);
    startButton.setAlignment(Pos.CENTER);

    CheckBox useBotCheckBox = new CheckBox("Use bot");
    useBotCheckBox.setAlignment(Pos.CENTER);

    vBoxBottom.setAlignment(Pos.CENTER);
    vBoxBottom.setSpacing(SPACING_2);
    vBoxBottom.getChildren().addAll(useBotCheckBox, startButton);
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
  }

  public void Game(int rowCount, int columnCount, int countOfMines, boolean useBot) {
    BorderPane root = new BorderPane();

    Board board = new Board();
    board.initCells(rowCount, columnCount, countOfMines, useBot);
    root.setCenter(board);

    Button restartBtn = new Button("Restart");
    restartBtn.setAlignment(Pos.CENTER);

    Text remainedBombs = new Text();
    remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));

    board.setOnMouseClicked(event -> {
      remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));
    });

    restartBtn.setOnMouseClicked(event -> {
      board.initCells(rowCount, columnCount, countOfMines, useBot);
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
}
