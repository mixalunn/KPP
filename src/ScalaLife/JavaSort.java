package ScalaLife;

import java.util.Random;


/** QSort on Java */
public class JavaSort {
  static Random rand = new Random();

  public void qSort(String[] arrayName, int[] numbFields, int begin, int end) {
    int i = begin;
    int j = end;
    int x = numbFields[(begin + end)/2];
    while (i <= j) {
      while (numbFields[i] > x) {
        i++;
      }
      while (numbFields[j] < x) {
        j--;
      }
      if (i <= j) {
        int tempInt = numbFields[i];
        String tempName = arrayName[i];
        numbFields[i] = numbFields[j];
        arrayName[i] = arrayName [j];
        numbFields[j] = tempInt;
        arrayName [j] = tempName;
        i++;
        j--;
      }
    }
    if (begin < j) {
      qSort(arrayName, numbFields,  begin, j);
    }
    if (i < end) {
      qSort(arrayName, numbFields, i, end);
    }
  }
}