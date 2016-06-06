package sample

/**
  * Created by yura_str on 6/6/16.
  */
object Translator {
  val generated = "Сгенерировано "
  val generatedWidth = " клеток по ширине, "
  val generatedHeight = " по высоте, и "
  val generatedBombs = " бомб.\n"
  val leftClick = "Левая кнопка: "
  val rightClick = "Правая кнопка: "
  val playerPress = "Игрок нажал на следуюшие кнопки по следующим координатам:\n"
  val bombPosition = "Бомбы сгенерировались по следующим координатам :\n"

  def parseStringBomb(array: Array[Int]): String = {
    var str = ""
    for (i <- array.indices) {
      if (i != array.length - 1 && i % 2 != 0)
        str += array(i).toString + "\n"
      else if (i != array.length - 1 && i % 2 == 0)
        str += array(i).toString + " и "
      else str += array(i).toString
    }
    str
  }
    def parseStringClick(array: Array[Int]): String = {
      var str = ""
      for (i <- array.indices) {
        if (i != array.length && i % 3 == 0)
          str += buttonReturn(array(i))
        else if (i != array.length && i % 3 == 1)
          str += array(i).toString + " и "
        else if (i != array.length && i % 3 == 2)
          str += array(i).toString + "\n"
      }
      str
    }

    def buttonReturn(click: Int): String = click match {
      case 0 => leftClick
      case 1 => rightClick
      case _ => "error"
    }
    def parse(replay: Array[Int], path: String) {
      writeLine(new Board(replay.take(3)).toString(), path)
      writeLine(new Bombs(replay.slice(3, replay.take(3).last * 2 + 3)).toString(), path)
      writeLine(new Clicks(replay.slice(replay.take(3).last * 2 + 3, replay.length)).
        toString(), path)
    }

    def writeLine(s: String, path: String) {
      var writer: java.io.BufferedWriter = null
      try {
        writer = new java.io.BufferedWriter(new java.io.FileWriter(path, true))
        writer.write(s)
        writer.newLine()
        writer.flush()
      } catch {
        case e: java.io.IOException => println(e)
      } finally {
        if (writer != null) {
          try {
            writer.close()
          } catch {
            case e: java.io.IOException => println(e)
          }
        }
      }
    }

    case class Board(board: Array[Int]) {
      override def toString = generated + board(0) + generatedWidth + board(1) +
        generatedHeight + board(2) + generatedBombs
    }
    case class Bombs(bombs: Array[Int]) {
      override def toString = bombPosition + parseStringBomb(bombs)
    }
    case class Clicks(clicks: Array[Int]) {
      override def toString = playerPress + parseStringClick(clicks)
    }
}
