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
    private Stage stage;

    public void Game(int rowCount, int columnCount, int countOfMines) {
        BorderPane root = new BorderPane();

        Board board = new Board();
        board.initCells(rowCount, columnCount, countOfMines);
        root.setCenter(board);

        Button restartBtn = new Button("Restart");
        restartBtn.setAlignment(Pos.CENTER);

        restartBtn.setOnMouseClicked(event -> {
            board.initCells(rowCount, columnCount, countOfMines);
        });

        Text remainedBombs = new Text();
        remainedBombs.setText("Bombs remained: " + String.valueOf(board.getRemainedBombs()));

        board.setOnMouseClicked(event -> {
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

        RadioButton radioButton0 = new RadioButton("Beginner");
        radioButton0.setSelected(true);
        radioButton0.setToggleGroup(toggleGroup);

        RadioButton radioButton1 = new RadioButton("Amateur");
        radioButton1.setToggleGroup(toggleGroup);

        RadioButton radioButton2 = new RadioButton("Professional");
        radioButton2.setToggleGroup(toggleGroup);

        VBox vBox = new VBox();
        vBox.getChildren().add(difficultText);
        vBox.getChildren().addAll(radioButton0, radioButton1, radioButton2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        borderPane.setCenter(vBox);

        HBox hBoxBottom = new HBox();
        Button startButton = new Button("Start");
        startButton.setPrefWidth(100);
        startButton.setPrefHeight(10);
        startButton.setAlignment(Pos.CENTER);
        hBoxBottom.setAlignment(Pos.CENTER);
        hBoxBottom.getChildren().add(startButton);
        hBoxBottom.setTranslateY(-20);
        borderPane.setBottom(hBoxBottom);

        startButton.setOnMouseClicked(event -> {
            if (radioButton0.isSelected()) {
                Game(9, 9, 10);
                return;
            }
            if (radioButton1.isSelected()) {
                Game(16, 16, 40);
                return;
            }
            if (radioButton2.isSelected()) {
                Game(16, 30, 99);
                return;
            }
        });

        Scene menuScene = new Scene(borderPane, 300, 300);

        stage = new Stage();
        stage.setTitle("Minesweeper");
        stage.setScene(menuScene);
        stage.show();
    }
}
