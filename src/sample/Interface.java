package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
  private final int SCENE_HEIGHT = 300;
  private final int SCENE_WIDTH = 300;
  private final int BUTTON_HEIGHT = 10;
  private final int BUTTON_WIDTH = 100;

  private final int BEGINNER_ROWS = 9;
  private final int BEGINNER_COLUMNS = 9;
  private final int BEGINNER_BOMBS = 9;

  private final int ADVANCED_ROWS = 16;
  private final int ADVANCED_COLUMNS = 16;
  private final int ADVANCED_BOMBS = 40;

  private final int PRO_ROWS = 16;
  private final int PRO_COLUMNS = 30;
  private final int PRO_BOMBS = 99;

  private Stage stage;

  public Interface(Stage stage) {
        this.stage = stage;
    }

  public void Game(int rowCount, int columnCount, int countOfMines) {
    BorderPane root = new BorderPane();

    Board board = new Board();
    board.initCells(rowCount, columnCount, countOfMines);
    root.setCenter(board);

    Button restartBtn = new Button("Restart");
    restartBtn.setAlignment(Pos.CENTER);

    Text remainedBombs = new Text();
    remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));

    board.setOnMouseClicked(event -> {
      remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));
      });

      restartBtn.setOnMouseClicked(event -> {
        board.initCells(rowCount, columnCount, countOfMines);
        remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));
      });

      HBox hBoxTop = new HBox();
      hBoxTop.setAlignment(Pos.CENTER);
      hBoxTop.getChildren().add(remainedBombs);
      hBoxTop.setTranslateY(10);

      HBox hBoxBottom = new HBox();
      hBoxBottom.setAlignment(Pos.CENTER);
      hBoxBottom.getChildren().add(restartBtn);
        hBoxBottom.setTranslateY(-15);

      root.setTop(hBoxTop);
      root.setBottom(hBoxBottom);

      Scene scene = new Scene(root, columnCount * 20 + 100, rowCount * 20 + 100);
      stage.setScene(scene);
      stage.show();
  }

  public void Menu() {
    BorderPane borderPane = new BorderPane();

    HBox hBoxTop = new HBox();
    Text welcomeText = new Text("Welcome to the game \"Minesweeper\"!");
    welcomeText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    hBoxTop.setAlignment(Pos.CENTER);
    hBoxTop.getChildren().add(welcomeText);
    hBoxTop.setTranslateY(10);
    borderPane.setTop(hBoxTop);

    Text difficultText = new Text("Select difficulty level");
    difficultText.setFont(Font.font(14));

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
    vBox.setSpacing(10);
    borderPane.setCenter(vBox);

    HBox hBoxBottom = new HBox();
    Button startButton = new Button("Start");
    startButton.setPrefWidth(BUTTON_WIDTH);
    startButton.setPrefHeight(BUTTON_HEIGHT);
    startButton.setAlignment(Pos.CENTER);
    hBoxBottom.setAlignment(Pos.CENTER);
    hBoxBottom.getChildren().add(startButton);
    hBoxBottom.setTranslateY(-20);
    borderPane.setBottom(hBoxBottom);

    Scene menuScene = new Scene(borderPane, SCENE_HEIGHT, SCENE_WIDTH);

    stage.setTitle("Minesweeper");
    stage.setScene(menuScene);
    stage.show();

    startButton.setOnMouseClicked(event -> {
      if (radioButtonBeginner.isSelected()) {
        Game(BEGINNER_ROWS, BEGINNER_COLUMNS, BEGINNER_BOMBS);
      }
      else
      if (radioButtonAdvanced.isSelected()) {
        Game(ADVANCED_ROWS, ADVANCED_COLUMNS, ADVANCED_BOMBS);
      }
      else
      if (radioButtonPro.isSelected()) {
        Game(PRO_ROWS, PRO_COLUMNS, PRO_BOMBS);
      }
    });
  }
}
