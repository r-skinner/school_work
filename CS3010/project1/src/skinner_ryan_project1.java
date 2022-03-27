import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class skinner_ryan_project1 {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.print("How many equations are there\n>");
        int n = userInput.nextInt();
        double[][] matrix = new double[n][n + 1];

        System.out.print("Would you like to input coefficient\n [1] by hand\n [2] by file\n>");
        int method;
        do {
            method = userInput.nextInt();
            if (method != 1 && method != 2) {
                System.out.print("Please enter only [1] or [2]\n [1] by hand\n [2] by file\n> ");
            }
        } while (method != 1 && method != 2);

        switch (method) {
            case 1:
                System.out.println("Enter your coefficients. e.g '1 2 -3 4' means 'x + 2y -3z = 4'");
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        matrix[i][j] = userInput.nextDouble();
                    }
                }
                break;
            case 2:
                System.out.print("Enter filename\n>");
                String name = userInput.next();
                File file = new File(name);
                try {
                    Scanner fileInput = new Scanner(file);
                    for (int i = 0; i < matrix.length; i++) {
                        for (int j = 0; j < matrix[i].length; j++) {
                            double d = fileInput.nextDouble();
                            matrix[i][j] = d;
                        }
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("File not found. Quitting.");

                }
                break;
        }
        System.out.print("Enter acceptable error\n>");
        double desiredError = userInput.nextDouble();

        System.out.println("Gaussian Elimination:");
        double[] ans = gaussian(matrix);
        for (int i = 0; i < ans.length; i++) {
            System.out.printf("\tx[%d] = %.2f%n", i, ans[i]);
        }


        System.out.println("\nJacobi Method:");
        ans = jacobi(matrix, desiredError);
        for (int i = 0; i < ans.length; i++) {
            System.out.printf("\t\tx[%d] = %.2f%n", i, ans[i]);
        }

        System.out.println("\nGauss-Seidel Method:");
        ans = gaussSeidel(matrix, desiredError);
        for (int i = 0; i < ans.length; i++) {
            System.out.printf("\t\tx[%d] = %.2f%n", i, ans[i]);
        }




    }

    private static void printMatrix(double[][] matrix) {
        for (double[] aMatrix : matrix) {
            for (double anAMatrix : aMatrix) {
                System.out.printf("\t%.2f ", anAMatrix);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static double[] gaussian(double[][] m) {

        double[][] matrix = new double[m.length][m[0].length];
        for(int i = 0; i < matrix.length; i ++){
            matrix[i] = m[i].clone();
        }

        //find largest values
        double[] largestValues = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            largestValues[i] = Math.abs(matrix[i][0]);
            for (int j = 1; j < matrix.length; j++) {
                double val = Math.abs(matrix[i][j]);
                if (val > largestValues[i]) {
                    largestValues[i] = val;
                }
            }
        }

        double[] ratios = new double[matrix.length];
        for (int k = 0; k < matrix.length - 1; k++) {
            //ratios of a[k]/max
            for (int i = k; i < matrix.length; i++) {
                ratios[i] = Math.abs(matrix[i][k] / largestValues[i]);
            }

            //maximum ratio to find pivot
            double maxRatio = ratios[k];
            int maxRatioIndex = k;
            for (int j = k + 1; j < ratios.length; j++) {
                if (ratios[j] > maxRatio) {
                    maxRatio = ratios[j];
                    maxRatioIndex = j;
                }
            }

            //make pivot row first
            double tmpRow[] = matrix[k];
            matrix[k] = matrix[maxRatioIndex];
            matrix[maxRatioIndex] = tmpRow;

            //eliminate all others
            for (int i = k + 1; i < matrix.length; i++) {
                double pivotRatio = matrix[i][k] / matrix[k][k];
                for (int j = k; j < matrix[i].length; j++) {
                    matrix[i][j] -= (pivotRatio) * matrix[k][j];
                }
            }
            printMatrix(matrix);
        }
        // solve
        double[] ans = new double[matrix.length];
        for (int i = matrix.length - 1; i >= 0; i--) {
            double otherVariables = 0;
            for (int j = i + 1; j < matrix.length; j++) {
                otherVariables += matrix[i][j] * ans[j];
            }
            ans[i] = (matrix[i][matrix.length] - otherVariables) / matrix[i][i];
        }

        return ans;
    }

    private static double[] jacobi(double[][] matrix, double desiredError) {
        //test if possible
        for(int i = 0; i < matrix.length; i ++){
            double sum = 0;
            for(int j = 0; j < matrix.length; j ++){
                if(i != j ){
                    sum += Math.abs(matrix[i][j]);
                }
            }
            if(Math.abs(matrix[i][i]) <= sum){
                System.out.println("This matrix is not diagonally dominant, it may not be solvable.");
                break;
            }
        }


        double error = desiredError + 1;
        double[] ans = new double[matrix.length];
        for(int i = 0; i < ans.length; i++ ){
            ans[i] = 0;
        }
        double[] old;

        for(int k = 0; k < 50; k++){
            old = ans.clone();
            for(int i = 0; i < matrix.length; i++){
                double sum = 0;
                for(int j = 0; j < matrix.length; j++){
                    if(i != j){
                        sum += matrix[i][j]*old[j];
                    }
                }
                ans[i] = (matrix[i][matrix.length]-sum)/matrix[i][i];
            }

            error = 0;
            for (int i = 0; i < ans.length; i++) {
                error += Math.pow(Math.abs(Math.abs(old[i]) - Math.abs(ans[i])),2);
            }
            error = Math.pow(error,0.5);

            System.out.printf("\tIteration %d:\n" , k);
            for (int i = 0; i < ans.length; i++) {
                System.out.printf("\t\tx[%d] = %.2f%n", i, ans[i]);
            }

            if(error <= desiredError){
                System.out.printf("\tSolution was found after %d iterations:\n",k);
                break;
            }
        }
        if(error > desiredError){
            System.out.println("\tNo solution was found after 50 iterations. Here is an approximation:");
        }
        return ans;
    }

    private static double[] gaussSeidel(double[][] matrix, double desiredError) {
        //test if possible
        for (int i = 0; i < matrix.length; i++) {
            double sum = 0;
            for (int j = 0; j < matrix.length; j++) {
                if (i != j) {
                    sum += Math.abs(matrix[i][j]);
                }
            }
            if (Math.abs(matrix[i][i]) <= sum) {
                System.out.println("This matrix is not diagonally dominant, it may not be solvable.");
                break;
            }
        }


        double error = desiredError + 1;
        double[] ans = new double[matrix.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = 0;
        }
        double[] old;

        for (int k = 0; k < 50; k++) {
            old = ans.clone();
            for (int i = 0; i < matrix.length; i++) {
                double sum = 0;
                for (int j = 0; j < matrix.length; j++) {
                    if (i != j) {
                        sum += matrix[i][j] * ans[j];
                    }
                }
                ans[i] = (matrix[i][matrix.length] - sum) / matrix[i][i];
            }

            error = 0;
            for (int i = 0; i < ans.length; i++) {
                error += Math.pow(Math.abs(Math.abs(old[i]) - Math.abs(ans[i])),2);
            }
            error = Math.pow(error,0.5);

            System.out.printf("\tIteration %d:\n", k);
            for (int i = 0; i < ans.length; i++) {
                System.out.printf("\t\tx[%d] = %.2f%n", i, ans[i]);
            }

            if (error <= desiredError) {
                System.out.printf("\tSolution was found after %d iterations:\n", k);
                break;
            }
        }
        if (error > desiredError) {
            System.out.println("\tNo solution was found after 50 iterations. Here is an approximation:");
        }
        return ans;
    }
}
/*
Extra credit: Test your program with large values of n, say like 20, 50, 100 etc.
Generate the coefficients randomly for such large equations.
Comments on the time taken by your program as the number of equations increase.
Do you run into any other problems? Write a one page report on this extra credit part.
Show a graph of how the time increases as the number of equations increase (3 curves on this graph,one for each method)
*/