package ScalaLife

/** QSort on Scala */
class ScalaSort {
  def sort(nameArray: Array[String], cellArray: Array[Int]) {
    def swap(a: Int, b: Int) {
      val tempCell = cellArray(a)
      val tempName = nameArray(a)
      cellArray(a) = cellArray(b)
      nameArray(a) = nameArray(b)
      cellArray(b) = tempCell
      nameArray(b) = tempName
    }

    def quickSort(begin: Int, end: Int) {
      val temp = cellArray((begin + end) / 2)
      var i = begin
      var j = end
      while (i <= j) {
        while (cellArray(i) > temp) {
          i += 1
        }
        while (cellArray(j) < temp) {
          j -= 1
        }
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (begin < j) quickSort(begin, j)
      if (j < end) quickSort(i, end)
    }
    quickSort(0, cellArray.length - 1)
  }
}
