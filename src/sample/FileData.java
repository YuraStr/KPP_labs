package sample;

public class FileData {
  private String date;
  private int bombs;
  private int rows;
  private int columns;

  public FileData(String date, int bombs, int rows, int columns) {
    this.date = date;
    this.bombs = bombs;
    this.rows = rows;
    this.columns = columns;
  }

  public String getDate() {
    return date;
  }

  public int getBombs() {
    return bombs;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }
}
