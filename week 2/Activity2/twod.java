package Activity2;

import java.util.Scanner;

public class twod {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double[][] twod = new double[3][4];

        System.out.println("Enter a 3-by-4 matrix row by row:");
        for (int r = 0; r < twod.length; r++) {
            for (int c = 0; c < twod[r].length; c++) {
                twod[r][c] = sc.nextDouble();
            }
        }

        double sum1 = sumColumn(twod, 0);
        System.out.println("Sum of the elements at column 0 is " + sum1);

        double sum2 = sumColumn(twod, 1);
        System.out.println("Sum of the elements at column 1 is " + sum2);

        double sum3 = sumColumn(twod, 2);
        System.out.println("Sum of the elements at column 2 is " + sum3);

        double sum4 = sumColumn(twod, 3);
        System.out.println("Sum of the elements at column 3 is " + sum4);
    }

    public static double sumColumn(double[][] m, int columnIndex){
        double total = 0;
        for (int r = 0; r < m.length; r++) {
            total += m[r][columnIndex];
        }
        return total;
    }

    public static double sumMajorDiagonal(double[][] m){
        double sum = 0;
        for (int r = 0; r < m.length; r++) {
            sum += m[r][r];
        }
        return sum;
    }
}