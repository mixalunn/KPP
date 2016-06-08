package ScalaLife

class ScalaStat {

  def getStat(fileName: Array[String], gameInfo: Array[Array[Int]]) {
    val tempArray = for (temp: Array[Int] <- gameInfo) yield temp
    val Max = new Array[Int](fileName.length)
    val Min = new Array[Int](fileName.length)
    var i = 0
    for (temp: Array[Int] <- tempArray)
       fillStat(temp)

    def fillStat(value: Array[Int]) {
      Max(i) = findMax(value, value.length);
      var MinField = 0
      var min = Max(i)
      var g = 0

      while (g < value.length) {
        if (value(g) < min) {
          min = value(g)
          MinField = g
        }
        g += 1;
      }
      Min(i) = MinField;

      i += 1
    }

    def findMax(stat: Array[Int], size: Int): Int = {
      var MaxField = 0
      var max = 0
      var g = 0

      while (g < size) {
        if (stat(g) > max) {
          max = stat(g)
          MaxField = g
        }
        g += 1;
      }
      MaxField+1
    }



    new StatTable( fileName, Max, Min)
  }

}
