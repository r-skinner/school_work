import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

public class driver {
    private static final double ALLOWED_ERROR = 0.01;
    private static final double DELTA = 0.01;
    private static final int MAX_ITERATIONS = 100;
    private static final double INITIAL_GUESS_MODIFIER = 1.2;

    public static void main(String[] args) throws IOException {

        Problem[] problems = new Problem[]{
                new Problem("f(x) = 2x^3 – 11.7x^2 + 17.7x – 5 ",           //function string
                        (x) -> 2*Math.pow(x,3) - 11.7*Math.pow(x,2) + 17.7*x - 5, //f(x)
                        (x) -> 6*Math.pow(x,2) - 23.4*x + 17.7,                   //f'(x)
                        new double[]{0.365,1.922,3.563}),                         //roots

                new Problem("f(x) = x + 10 – x*cosh(50/x)",           //function string
                        (x) -> x + 10 - x*Math.cosh(50/x),                  //f(x)
                        (x) -> 1 + 50/x*Math.sinh(50/x)-Math.cosh(50/x),    //f'(x)
                        new double[]{126.632}),                             //roots
        };


        System.out.println("Finding roots for functions:");
        Solution solution;
        for(Problem problem : problems){
            for(double root : problem.roots){
                System.out.printf("Root: %.2f for function: %s%n", root, problem.string);
                double pointA = root/ INITIAL_GUESS_MODIFIER;
                double pointB = root* INITIAL_GUESS_MODIFIER;

                solution = bisection(problem.function,pointA,pointB,root);
                createCSV(root + "_bisection.csv", solution);

                solution = falsePosition(problem.function,pointA,pointB,root);
                createCSV(root  + "_falsePosition.csv", solution);

                solution = newtonRapson(problem.function, problem.derivative, pointA, root);
                createCSV(root + "_newtonRapson.csv", solution);

                solution = secant(problem.function, pointA, pointB, root);
                createCSV(root + "_secant.csv", solution);

                solution = modifiedSecant(problem.function, pointA, root);
                createCSV(root + "_modifiedSecant.csv", solution);
            }
        }
    }

    private static void createCSV(String filename, Solution solution) throws IOException {
        filename = "csv/"+filename;
        FileWriter writer = new FileWriter(filename);
        writer.write("Iteration,True Error,Relative Error\n");

        for(int i = 0; i < solution.iterations+1; i++) {
            writer.write(String.format("%d,%.4f,%.4f\n", i, solution.trueError[i], solution.relError[i]));
        }

        writer.close();
        System.out.println("Output to file: " + filename);
    }

    private static Solution bisection(Function<Double,Double> function, double pointA, double pointB, double trueRoot){
        Solution solution = new Solution();
        int n = 0;
        double oldC = Double.POSITIVE_INFINITY;
        double pointC;
        while(n < MAX_ITERATIONS){
            pointC = (pointA + pointB)/2;

            solution.relError[n] = Math.abs((oldC-pointC)/oldC);
            solution.trueError[n] = Math.abs((trueRoot-pointC)/trueRoot);

            if(solution.relError[n] < ALLOWED_ERROR) {
                solution.x = pointC;
                solution.solutionFound = true;
                solution.iterations = n;
                return solution;
            }

            if(Math.signum(function.apply(pointA)) == Math.signum(function.apply(pointC))) {
                pointA = pointC;
            }
            else{
                pointB = pointC;
            }

            oldC = pointC;
            n++;
        }
        return solution;
    }

    private static Solution falsePosition(Function<Double,Double> function, double pointA, double pointB, double trueRoot){
        Solution solution = new Solution();
        int n = 0;
        double oldC = Double.POSITIVE_INFINITY;
        double pointC;
        while(n < MAX_ITERATIONS){
            pointC = (pointA*function.apply(pointB) - pointB*function.apply(pointA))/(function.apply(pointB) - function.apply(pointA));

            solution.relError[n] = Math.abs((oldC-pointC)/oldC);
            solution.trueError[n] = Math.abs((trueRoot-pointC)/trueRoot);

            if(solution.relError[n] < ALLOWED_ERROR) {
                solution.x = pointC;
                solution.solutionFound = true;
                solution.iterations = n;
                return solution;
            }

            if(Math.signum(function.apply(pointA)) == Math.signum(function.apply(pointC))) {
                pointA = pointC;
            }
            else{
                pointB = pointC;
            }

            oldC = pointC;
            n++;
        }
        return solution;
    }

    private static Solution newtonRapson(Function<Double,Double> function, Function<Double,Double> derivative, double point, double trueRoot){
        Solution solution = new Solution();
        double ep = 1E-5;
        int n = 0;
        double point2;
        double y;
        double yPrime;
        while(n < MAX_ITERATIONS){
            y  = function.apply(point);
            yPrime = derivative.apply(point);
            point2 = point - y/yPrime;

            solution.relError[n] = Math.abs((point-point2)/point);
            solution.trueError[n] = Math.abs((trueRoot-point2)/trueRoot);

            if(Math.abs(yPrime) < ep) return solution;

            if(solution.relError[n] < ALLOWED_ERROR) {
                solution.x = point2;
                solution.solutionFound = true;
                solution.iterations = n;
                return solution;
            }
            point = point2;
            n++;
        }
        return solution;
    }

    private static Solution secant(Function<Double,Double> function, double pointA, double pointB, double trueRoot){
        Solution solution = new Solution();
        int n = 0;
        double pointC;
        while(n < MAX_ITERATIONS){
            pointC = pointB - function.apply(pointB)*(pointB-pointA)/(function.apply(pointB)-function.apply(pointA));

            solution.relError[n] = Math.abs((pointB-pointC)/pointC);
            solution.trueError[n] = Math.abs((trueRoot-pointC)/trueRoot);

            if(solution.relError[n] < ALLOWED_ERROR) {
                solution.x = pointC;
                solution.solutionFound = true;
                solution.iterations = n;
                return solution;
            }
            pointA = pointB;
            pointB = pointC;
            n++;
        }
        return solution;
    }

    private static Solution modifiedSecant(Function<Double,Double> function, double pointA, double trueRoot){
        Solution solution = new Solution();
        int n = 0;
        double pointB;
        while(n < MAX_ITERATIONS){
            pointB = pointA - function.apply(pointA)*(pointA*DELTA)/(function.apply(pointA + pointA*DELTA)-function.apply(pointA));

            solution.relError[n] = Math.abs((pointB-pointA)/pointB);
            solution.trueError[n] = Math.abs((trueRoot-pointB)/trueRoot);

            if(solution.relError[n] < ALLOWED_ERROR) {
                solution.x = pointB;
                solution.solutionFound = true;
                solution.iterations = n;
                return solution;
            }
            pointA = pointB;
            n++;
        }
        return solution;
    }

    static class Solution{
        boolean solutionFound;
        double[] trueError;
        double[] relError;
        double x;
        int iterations;
        Solution(){
            solutionFound = false;
            trueError = new double[MAX_ITERATIONS];
            relError = new double[MAX_ITERATIONS];
        }
    }

    static class Problem{
        Function<Double,Double> function;
        Function<Double,Double> derivative;
        String string;
        double[] roots;

        Problem(String string, Function<Double,Double> function, Function<Double,Double> derivative, double[] roots){
            this.function = function;
            this.derivative = derivative;
            this.roots = roots;
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }
}

/*
Please upload the code, the executable and the report to blackboard.
Please bring a printed of the report (with your name and class and project number on it on the first page of the report)

1. check for errors early
6. mention starting points
7. interesting behaviors
8. talk about data types
*/