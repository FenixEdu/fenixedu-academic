package middleware.dataClean.personFilter;

public class SimilarWaterman {

    /* the matrix to store the distances in the Ukonnen simplifications*/
    static int distances[][] = new int[2][5];

    static int distMatrix[][] = new int[1024][2];

    static int line1[] = new int[1024];
    static int line2[] = new int[1024];


    private static int DIFF(int x, int y) {
      if (x > y)
        return(x - y);
      else return(y - x);
    }

    /* computes the minimum value of two numbers */
    private static int min(int number1, int number2) {
      return number1 < number2 ? number1 : number2;
    }

    /* computes the minimum value of three numbers */
    private static int min3(int number1, int number2, int number3) {
      int min;

      if (number2 < number1)
        min = number2;
      else
        min = number1;

      if (number3 < min)
        return number3;

      return min;
    }

    /* Ukkonen algorithm*/
    /**
     * Implements the Ukkonen algorithm. It assumes that the smallest
     * string to compare is given in the str1 argument.
     * Returns the distance between the two strings or maxDist +1 if the
     * distance between both is greater than maxDist. Works for maxDist <= 2
     *
     * The matrix of distances is only calculated for the main diagonal and
     * the other two.
     *
     * @argum str1 is the first string. This must be the smallest string,
     *        or this function will does not work.
     * @argum len1 is the size of str1.
     * @argum str2 is the second string. This one must the bigger string.
     * @argum len2 is the size of str2.
     * @argum maxDist is the maximal allowed distance between both strings.
     *
     * @return the distance between the strings, or maxDist + 1 if the distance
     * is greater than maxDist.
     **/
    public static int ukonnen1(char str1[], int len1, char str2[], int len2,
                                 int maxDist) {
    int first[], second[], tmp[];
    int i;

    /* initialization of the first column and first element of 2nd
       column of the matrix */

    /* version for maxDist =1 */
      distances[0][0] = 0;
      distances[0][1] = distances[1][0] = 1; /* it seems wrong but it isn't!! */

      second = distances[1];
      first = distances[0];

      /* initialization of the rest of 2nd column of the matrix. This is slightly
         different from the initialization done inside the cicle for. */
      second[1] = min3(first[1] + 1, second[0] + 1,
                       first[0] + (str1[0] == str2[0] ? 0 : 1));
      second[2] = min(second[1] + 1, first[1] + (str1[0] == str2[1] ? 0 : 1));

      if (len1 == 1) {
        if (len1 != len2)
          return min(maxDist+1, second[2]);

        return min(maxDist+1, second[1]);
      }

      for (i = 1; i < len1 - 1; i++) {
        tmp = first;
        first = second;
        second = tmp; /* change columns */

        second[0] = min(first[0] + (str1[i] == str2[i - 1] ? 0 : 1), first[1] + 1);
        second[1] = min3(first[2] + 1, second[0] + 1,
                         first[1] + (str1[i] == str2[i] ? 0 : 1));
        second[2] = min(second[1] + 1, first[2] + (str1[i] == str2[i + 1] ? 0 : 1));

        /* test to see if the current distances are still bellow the max allowed distance */
        if (second[0] > maxDist && second[1] > maxDist && second[2] > maxDist)
          return maxDist + 1;
      }

      tmp = first;
      first = second;
      second = tmp; /* change columns */

      second[0] = min(first[0] + (str1[i] == str2[i - 1] ? 0 : 1), first[1] + 1);
      second[1] = min3(first[2] + 1, second[0] + 1,
                       first[1] + (str1[i] == str2[i] ? 0 : 1));


      if (len1 != len2) {
        second[2] = min(second[1] + 1, first[2] + (str1[i] == str2[i + 1] ? 0 : 1));
        return min(maxDist+1, second[2]);
      }

      return min(maxDist+1, second[1]);

  }

  /* Ukkonen algorithm*/
  /**
   * Implements the Ukkonen algorithm. It assumes that the smallest
   * string to compare is given in the str1 argument.
   * Returns the distance between the two strings or maxDist +1 if the
   * distance between both is greater than maxDist. Works for maxDist <= 2
   *
   * The matrix of distances is only calculated for the main diagonal and
   * the other two.
   *
   * @argum str1 is the first string. This must be the smallest string,
   *        or this function will does not work.
   * @argum len1 is the size of str1.
   * @argum str2 is the second string. This one must the bigger string.
   * @argum len2 is the size of str2.
   * @argum maxDist is the maximal allowed distance between both strings.
   *
   * @return the distance between the strings, or maxDist + 1 if the distance
   * is greater than maxDist.
   **/
  public static int ukonnen2(char str1[], int len1, char str2[], int len2,
       int maxDist) {
    int first[], second[], tmp[];
    int i;

    /* initialization of the first column and first element of 2nd
       column of the matrix */

      int n, k = len1 == len2 ? 1 : 0;

      for (i = 0; i < 3; i++)
        distances[0][i] = i;

      distances[1][0] = 1;

      second = distances[1];
      first = distances[0];

      /* initialization of the rest of 2nd column of the matrix. This is slightly
         different from the initialization done inside the cicle for. */
      second[1] = min3(first[1] + 1, second[0] + 1,
                       first[0] + (str1[0] == str2[0] ? 0 : 1));
      second[2] = min3(first[2] + 1, second[1] + 1,
                       first[1] + (str1[0] == str2[1] ? 0 : 1));
      second[3] = min3(first[3] + 1, second[2] + 1,
                       first[2] + (str1[0] == str2[2] ? 0 : 1));

      if (len1 == 1)
        return min(maxDist+1, second[len2]);

      for (i = 1; i < len1; i++) {
        tmp = first;
        first = second;
        second = tmp; /* change columns */

        if (i == 1) {
          second[0] = 2;
          second[1] = min3(first[1] + 1, second[0] + 1,
                           first[0] + (str1[1] == str2[0] ? 0 : 1));
          second[2] = min3(first[2] + 1, second[1] + 1,
                           first[1] + (str1[1] == str2[1] ? 0 : 1));
          second[3] = min3(first[3] + 1, second[2] + 1,
                           first[2] + (str1[1] == str2[2] ? 0 : 1));
          if (len2 < 3)
            return min(maxDist+1, second[len2]);

          second[4] = min(second[3] + 1, first[3] + (str1[1] == str2[2] ? 0 : 1));
        }
        else {
          second[0] = min(first[0] + (str1[i] == str2[i - 2] ? 0 : 1), first[1] + 1);

          second[1] = min3(first[2] + 1, second[0] + 1,
                           first[1] + (str1[i] == str2[i - 1] ? 0 : 1));
          second[2] = min3(first[3] + 1, second[1] + 1,
                           first[2] + (str1[i] == str2[i] ? 0 : 1));
          if (i < len1 - k)
            second[3] = min3(first[3] + 1, second[2] + 1,
                             first[3] + (str1[i] == str2[i+1] ? 0 : 1));
          if (i < len1 - k - 1)
            second[4] = min(second[3] + 1, first[4] + (str1[i] == str2[i + 2] ? 0 : 1));
        }
        /* test to see if the current distances are still bellow the max allowed distance */
        for (n = 0; n < 5; n++)
          if (second[n] <= maxDist)
            break;

        if (n == 5)
          return maxDist + 1;
      }

      if (len1 != len2)
        return min(maxDist+1, second[3]);
      return min(maxDist+1, second[2]);

  }

  public int sw(char str1[], char str2[], int len1, int len2, int maxDist) {

    //System.out.println("entering sw: str1: " +  String.valueOf(str1, 0, len1)+ " str2: " + String.valueOf(str2, 0, len2)+ " len1: " + len1 + " len2: " + len2 +  " maxDist: " + maxDist );

    /* optimization: just allocate two columns of the matrix*/

    int temp[];
    int i, j, n;
    //System.out.println("maxDist: " + maxDist);
    //System.out.println("len1: " + len1 + " len2: " + len2);

    distMatrix[0] = line1;
    distMatrix[1] = line2;

    /* initialize left top corner */
    /* optimization: delete one */
    distMatrix[0][0] = 0;

    /* initialize first column */
    for (j = 1; j <= len2; j++)
      distMatrix[0][j] = j;

    /* just keep one column for computing another one */
    for (i = 1; i <= len1; i++) {

      distMatrix[1][0] = i;
      for (j = 1; j <= len2; j++)
        distMatrix[1][j] =
          min3(distMatrix[0][j] + 1, distMatrix[1][j-1] + 1,
               distMatrix[0][j-1] + ((str1[i-1] != str2[j-1])? 1 : 0));

      /* it is not the last time */
      if (i < len1) {
        /* test to see if at least one of the distances in the
           current matrix is bellow the maxDist, otherwise stop */
        for (n = 0; n <= len2; n++) {
          if (distMatrix[1][n] <= maxDist)
            break;
        }

        if (n == len2 + 1)  {// every distance is higher.
          //System.out.println("maxDist + 1");
          return maxDist + 1;
        }

        temp = distMatrix[0];
        distMatrix[0] = distMatrix[1];
        distMatrix[1] = temp;
      }
    }

    /* minimum distance between str1 and str2 is given by the bottom right
       corner */
    return min(maxDist+1, distMatrix[1][len2]);
  }

  /* Smith-Waterman algorithm with Ukonnen simplification*/
  public int computeDistance(char str1[], char str2[], int len1, int len2, int maxDist) {
    int lenDiff;

    /*if (abs(len1 - len2) > maxDist)
      return maxDist + 1;*/

    /* test if Ukonnen can be called */
    if (maxDist <= 2) {
      lenDiff = DIFF(len1, len2);

      if ((lenDiff <= 1) && (maxDist <= 1) && (len1 > 1) && (len2 > 1))  {
        if (len1 <= len2) {
          //System.out.println("ukonnen1");
          return ukonnen1(str1, len1, str2, len2, maxDist);
        }
        else {
          //System.out.println("ukonnen1");
          return ukonnen1(str2, len2, str1, len1, maxDist);
        }
      } else
        if ((lenDiff <= 1) && (maxDist <= 2) && (len1 > 3) && (len2 > 3)) {
          if (len1 <= len2) {
            //System.out.println("ukonnen2");
            return ukonnen2(str1, len1, str2, len2, maxDist);
          }
          else {
            //System.out.println("ukonnen2");
            return ukonnen2(str2, len2, str1, len1, maxDist);
          }
        }
    }
    //System.out.println("SW");
    if (len1 > len2)
      return sw(str1, str2, len1, len2, maxDist);
    else
      return sw(str2, str1, len2, len1, maxDist);
  }

  /*  public static void main(String arg[]) {

      //String str1 = new String("ola1");
       // String str2 = new String("ola2");
      String str2 = new String("d randall");
      String str1 = new String("randall h");
      int dist = 0;
      char[] sstr1 = new char[1024];
      char[] sstr2 = new char[1024];
      str1.getChars(0, str1.length(), sstr1, 0);
      str2.getChars(0, str2.length(), sstr2, 0);
      dist = sw(sstr1, sstr2, str1.length(), str2.length(), 2);
      //dist = SmithWaterman.computeDistance(sstr1, sstr2, str1.length(), str2.length(), 2);
      System.out.println("dist: " + dist);
      dist = sw(sstr2, sstr1, str2.length(), str1.length(), 2);
      //dist = SmithWaterman.computeDistance(sstr2, sstr1, str2.length(), str1.length(), 2);
      System.out.println("dist: " + dist);
    }*/
  }
