import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class mainTest {

    public static void main(String[] args) throws FileNotFoundException{
        int[] ns = new int[]{10,20,40,50,100,200,500};
        int iterations = 100000;
        double desiredError = 1;
        Random random = new Random();

        PrintWriter printWriter = new PrintWriter(new File("output1.csv"));
        printWriter.printf("n,avg g time, avg j time, avg gs time, avg j iters, avg gs iters\n");
        for(int n : ns) {
            double[][] matrix = new double[n][n + 1];

            long gTotalTime = 0;
            long jTotalTime = 0;
            long gsTotalTime = 0;
            int jIters = 0;
            int gsIters = 0;

            for(int k = 0; k < iterations; k++){
                System.out.printf("n = %d, k = %d\n", n, k);

                //generate matrix
                for(int i = 0; i < n; i ++){
                    for(int j = 0; j < n+1; j++){
                        matrix[i][j] = random.nextDouble()*n*10;
                    }
                }

                long endTime;
                long startTime = System.nanoTime();
                gaussian(matrix);
                endTime = System.nanoTime();
                gTotalTime += (endTime - startTime);


                int temp;
                startTime = System.nanoTime();
                temp = jacobi(matrix, desiredError);
                endTime = System.nanoTime();
                jTotalTime += (endTime - startTime);
                jIters += (temp < 0 ? 0 : temp);


                startTime = System.nanoTime();
                temp = gaussSeidel(matrix, desiredError);
                endTime = System.nanoTime();
                gsTotalTime += (endTime - startTime);
                gsIters += (temp < 0 ? 0 : temp);
            }
            printWriter.printf("%d,%f,%f,%f,%f,%f\n",
                    n,
                    gTotalTime*1.0/iterations,
                    jTotalTime*1.0/iterations,
                    gsTotalTime*1.0/iterations,
                    jIters*1.0/iterations,
                    gsIters*1.0/iterations);
        }
        printWriter.close();
    }

    private static void gaussian(double[][] m) {

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
    }

    private static int jacobi(double[][] matrix, double desiredError) {
        double error;
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
            for(int i = 0; i < ans.length; i ++){
                error += Math.abs(Math.abs(old[i])-Math.abs(ans[i]));
            }

            if(error <= desiredError){
                return k;
            }
        }
        return -1;
    }

    private static int gaussSeidel(double[][] matrix, double desiredError) {
        double error;
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
            if (error <= desiredError) {
                return k;
            }
        }
            return -1;
    }
}
