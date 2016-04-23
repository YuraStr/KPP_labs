package sample;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class
 */
public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Interface anInterface = new Interface(primaryStage);
    anInterface.Menu();
  }

  public static void main(String[] args) {
        launch(args);
    }
}
