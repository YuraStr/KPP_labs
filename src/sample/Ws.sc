def rotateNumber(n : Int) : Int = {
  var temp = n
  var res = 0
  while (temp > 0) {
    res = res * 10 + temp % 10
    temp /= 10
  }
  res
}

rotateNumber(257851)


def delete(list: List[Int], n: Int): List[Int] = {
  list filter(x => x != n)
}

delete(List(1,2,4,5,5,5,4,2,5), 5)

def addLists(list1: List[Int], list2: List[Int]): Seq[Int] = {
  for (i <- list1.indices) yield list1(i) + list2(i)
}

addLists(List(6,2,3), List(4,5,6))

