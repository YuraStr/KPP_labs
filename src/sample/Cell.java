package sample;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Cell extends StackPane{
  private final int CELL_WIDTH = 20;
  private final int CELL_HEIGHT = 20;

  private final int IMG_HEIGHT = 15;
  private final int IMG_WIDTH = 15;

  private final String BOMB_IMAGE_PATH = "img/imgBomb.png";
  private final String FLAG_IMAGE_PATH = "img/imgRedFlag.png";


  private boolean bomb;
  private boolean open;
  private boolean flag;
  private int neighbors;
  private Rectangle rect;
  private Label label;

  public Cell() {
    bomb = false;
    open = false;
    neighbors = 0;

    rect = new Rectangle(CELL_WIDTH, CELL_HEIGHT, Color.LIGHTGRAY);
    rect.setStroke(Color.GRAY);

    label = new Label();
    label.setFont(Font.font(14));
    label.setText(null);
    label.setVisible(false);

    getChildren().addAll(rect, label);
  }

  public void setBomb(boolean b) {
    bomb = b;
    if (bomb) {
      setImage(BOMB_IMAGE_PATH);
    }
  }

  public void addNeighbor() {
        neighbors++;
    }

  public void open() {
    if (isOpen())
      return;

    open = true;
    rect.setFill(Color.WHITESMOKE);
    label.setVisible(true);

    if (label.getText() != null) {
      switch (neighbors) {
        case 1:
          label.setTextFill(Color.BLUE);
          break;
        case 2:
          label.setTextFill(Color.GREEN);
          break;
        case 3:
          label.setTextFill(Color.RED);
          break;
        case 4:
          label.setTextFill(Color.DARKBLUE);
          break;
        case 5:
          label.setTextFill(Color.BROWN);
          break;
        case 6:
          label.setTextFill(Color.BURLYWOOD);
          break;
        case 7:
          label.setTextFill(Color.NAVY);
          break;
        case 8:
          label.setTextFill(Color.CHOCOLATE);
          break;
      }
    }
  }

  public boolean isBomb() {
        return bomb;
    }

  public boolean isOpen() { return open; }

  public boolean isFlag() { return flag; }

  public boolean isEmpty() {
        return label.getText() == null;
    }

  public void setText() {
    if (!bomb && neighbors != 0) {
      label.setText(String.valueOf(neighbors));
    }
  }

  public void onBomb() {
    rect.setFill(Color.RED);
    label.setVisible(true);
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
    if (flag) {
      setImage(FLAG_IMAGE_PATH);
      label.setVisible(true);
    }
    else {
      if (bomb) {
        setImage(BOMB_IMAGE_PATH);
        label.setVisible(false);
      }
      else {
        label.setGraphic(null);
        label.setVisible(false);
        setText();
      }
    }
  }

  public void setImage(String path) {
    Image image = new Image(getClass().getResourceAsStream(path));
    ImageView img = new ImageView(image);
    img.setFitHeight(IMG_HEIGHT);
    img.setFitWidth(IMG_WIDTH);
    label.setGraphic(img);
    label.setText(null);
  }
}
