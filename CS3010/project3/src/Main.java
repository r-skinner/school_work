import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        double[] x = new double[50];
        double[][] y = new double[50][50];
        int size = 0, xSize = 0, ySize = 0;


        //read file
        try{
            //args = new String[]{"input2.txt"};
            if(args.length != 1) System.out.println("Please input a file as an argument");

            Scanner fileReader = new Scanner(new File(args[0]));

            for(String i: fileReader.nextLine().split(" ")) x[xSize++] = Double.parseDouble(i);

            for(String i: fileReader.nextLine().split(" ")) y[0][ySize++] = Double.parseDouble(i);

            if(xSize != ySize){
                System.out.println("Error input file. Mismatched values");
                System.exit(0);
            }
            else{
                size = xSize;
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found.");
            System.exit(0);
        }

        //Calculate values
        for(int i = 1; i < size; i++){
            int startingX = i;
            for(int j = 0; j < size-i; j++){
                y[i][j] = (y[i-1][j+1] - y[i-1][j]) / (x[startingX]-x[j]);
                startingX++;
            }
        }

        //Print header
        System.out.print("x\t\tf[]\t\t");
        for(int i = 0; i < size-1; i++){
            System.out.printf("f[%s]\t", new String(new char[i+1]).replace("\0", ","));
        }

        //Print values
        for(int i = 0; i < size;i++){
            System.out.printf("\n%.2f",x[i]);
            for(int j = 0; j < size-i; j++){
                System.out.printf("\t%.2f",y[j][i]);
            }
        }

        //print polynomial
        System.out.printf("\n\nInterpolating polynomial is:\n%.2f", y[0][0]);
        for(int i = 1; i < size; i++){
            if(Math.abs(y[i][0]) > .005) {
                System.out.printf(" + %.2f", y[i][0]);
                for (int j = 1; j <= i; j++) {
                    if(Math.abs(x[j - 1]) > .005) {
                        System.out.printf("(x + %.2f)", x[j - 1] * -1);
                    }
                    else{
                        System.out.print("(x)");
                    }
                }
            }
        }

        //simplified polynomial
        System.out.print("\n\nSimplified polynomial is:\n");

        double[] output = new double[size];
        for(int i = 0; i < size; i++){
            double[] polynomial = new double[]{0,y[i][0]};
            for(int j = 1; j <= i; j++){
                polynomial = multiplyPolynomial(polynomial, new double[]{1, x[j-1]*-1 });
            }
            int k = 0;
            for (int j = polynomial.length - 1; j > 0; j--) {
                output[k++] += polynomial[j];
            }
        }

        //print
        for(int i = output.length - 1; i >= 0; i--){
            if(Math.abs(output[i]) > .005) {
                System.out.printf("%.2f", output[i]);
                if (i > 0) System.out.printf("x^%d + ", i);
            }
        }
    }

    private static double[] multiplyPolynomial(double[] A, double[] B){
        double[] prod = new double[A.length + 2 - 1];
        for(int i = 0; i < A.length + 2 - 1; i++){
            prod[i] = 0;
        }
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < 2; j++)
            {
                prod[i + j] += A[i] * B[j];
            }
        }
        return prod;
    }
}
