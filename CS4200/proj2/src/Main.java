import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int n = 25;
        int iters = 100000;

        //*/

        Annealing a = new Annealing(n, true);
        System.out.print("Annealing result: ");
        a.solve();
        Genetic g = new Genetic(n, true);
        System.out.print("Genetic result:   ");
        g.solve();

        /*/

        solve(iters,n);

        //*/
    }
    private static void solve(int iters, int n){
        long gaTime = 0;
        int gaSuccess = 0;
        double gaIters = 0;

        long saTime = 0;
        int saSuccess = 0;
        double saIters = 0;

        for(int i = 0; i < iters; i++){
            System.out.printf("%d\n",i);
            Genetic g = new Genetic(n);
            Annealing a = new Annealing(n);

            long gaStartTime = System.nanoTime();
            int ga = g.solve();
            long gaStopTime = System.nanoTime();

            long saStartTime = System.nanoTime();
            int sa = a.solve();
            long saStopTime = System.nanoTime();


            if(ga > 0){
                gaTime = gaStopTime - gaStartTime;
                gaSuccess++;
                gaIters += ga;
            }
            if(sa > 0){
                saTime = saStopTime - saStartTime;
                saSuccess++;
                saIters += sa;
            }
        }
        System.out.printf("In %d tests GA solved %d puzzles, a %.2f%% success rate. ", iters, gaSuccess, (gaSuccess*100.0)/iters);
        System.out.printf("It took an average of %d nanoseconds and %d iterations to solve.\n", gaTime/gaSuccess, (int)gaIters/gaSuccess);

        System.out.printf("In %d tests SA solved %d puzzles, a %.2f%% success rate. ", iters, saSuccess, (saSuccess*100.0)/iters);
        System.out.printf("It took an average of %d nanoseconds and %d iterations to solve.\n", saTime/saSuccess, (int)saIters/saSuccess);
    }
}

class Annealing{
    private Board board;
    private Random random;
    private boolean verbose = false;

    private float temperature = 1;
    private float decayRate = 0.9998f;
    private float threshold = 0.00005f;


    Annealing(int n){
        this.board = new Board(n);
        this.random = new Random();
    }

    Annealing(int n, boolean verbose){
        this.board = new Board(n);
        this.random = new Random();
        this.verbose = verbose;
    }

    /*
    returns the number of iterations it took to find the solution or -1 if no solution is found
     */
    int solve(){
        int iters = 0;

        while(temperature > threshold && board.getCost() !=0) {
            Board next = board.mutate();                    //generate next board
            int delta = board.getCost() - next.getCost();
            if(delta > 0){
                board = next;
            }
            else {
                double chance = Math.exp(delta / temperature);  //e^(d/t)
                if (random.nextFloat() <= chance) board = next;
            }

            temperature *= decayRate;   // temperature schedule
            iters++;
        }

        if(verbose) {
            if (board.getCost() == 0) System.out.println(board);
            else System.out.println("Solution not found.");
        }

        return board.getCost() == 0 ? iters : -1;
    }
}

/*
There is no fitness function because a decent function would be costOfBoard/costSumOfPopulation
and why waist the time iterating and dividing for a constant ratio?
 */
class Genetic{
    private int n;
    private ArrayList<Board> population;
    private Random random;
    private boolean verbose = false;

    private int populationSize = 48;

    Genetic(int n){
        this.n = n;
        this.population = new ArrayList<>(populationSize);
        random = new Random();
        generateInitialPopulation();
    }
    Genetic(int n, boolean verbose){
        this.n = n;
        this.population = new ArrayList<>(populationSize);
        random = new Random();
        this.verbose = verbose;
        generateInitialPopulation();
    }

    private void generateInitialPopulation(){
        for(int i = 0; i < populationSize; i++){
            population.add(new Board(n));
        }
    }

    private Board reproduce(Board parent1, Board parent2){
        int crossOverPoint = random.nextInt(n);
        int[] child = new int[n];

        System.arraycopy(parent1.getBoard(), 0, child, 0, crossOverPoint); // from p1
        System.arraycopy(parent2.getBoard(), crossOverPoint, child, crossOverPoint, n - crossOverPoint); //from p2

        return new Board(child);
    }

    /*
    returns the number of iterations it took to find the solution or -1 if no solution is found
     */
    int solve(){
        for(int iterations = 0; iterations < 1750; iterations ++){ //1750 number of generations
            ArrayList<Board> newPop = new ArrayList<>(populationSize);

            //* Not so random choosing, less time for a similar result
            for(int i = 0; i < 4; i++){ // top 4
                for(int j = i+1; j < 4; j++){ // top 4
                    for(int k = 0; k < 4; k++){ //4 times twice = 8, to fill population
                        Board child = reproduce(population.get(i), population.get(j));
                        if (random.nextFloat() < 0.90f)  child = child.mutate(); //90% mutation rate
                        newPop.add(child);

                        child = reproduce(population.get(j), population.get(i));
                        if (random.nextFloat() < 0.90f)  child = child.mutate(); //90% mutation rate
                        newPop.add(child);
                    }
                }
            }

            /*/ random parents, possible a-sexual reproduction
            for(int i = 0; i < populationSize; i++) {
                //pick parents
                Board p1 = population.get(random.nextInt((int)(.08*populationSize)));
                Board p2 = population.get(random.nextInt((int)(.08*populationSize)));
                //create child
                Board child = reproduce(p1, p2);
                //mutate
                float r = random.nextFloat();
                if (r < .9f)  child = child.mutate();
                newPop.add(child);
            }
            //*/

            population = newPop;
            //sort lowest cost first
            //this could be sped up by using merge sort to find the kth element(4th in this case) and only sort those k
            population.sort(new Comparator<>() {

                @Override
                public int compare(Board o1, Board o2) {

                    return o1.getCost() - o2.getCost();
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            });

            if(population.get(0).getCost() == 0){
                if(verbose) System.out.println(population.get(0));
                return populationSize*iterations;
            }

        }
        if (verbose) System.out.println("Solution not found.");
        return -1;
    }
}

class Board{

    private int[] board;
    private int cost;

    Board(int n){
        Random random = new Random();
        board = new int[n];
        for(int i = 0; i < n; i ++){
            board[i] = random.nextInt(n);
        }
        cost = findCost();
    }

    Board(int[] board){
        this.board = board;
        this.cost = findCost();
    }


    /*
    The cost is found and stored per board because recalculating (in genetic) would be a huge waste of time
     */
    private int findCost(){
        int total = 0;
        for(int i = 0; i < board.length; i ++){
            for(int j = i; j < board.length; j ++){
                if(i != j){
                    if(board[i] == board[j]) total ++; //same row
                    if(Math.abs(i-j) == Math.abs(board[i]-board[j])) total++;//diagonal
                }
            }
        }
        return total;
    }

    int getCost(){
        return cost;
    }

    int[] getBoard(){
        return board;
    }

    /*
    Picks a random row and moves the queen to a new position
     */
    Board mutate(){
        Random random = new Random();
        int[] b = board.clone();

        int randCol = random.nextInt(b.length);
        int randRow;
        do{
            randRow = random.nextInt(b.length); // actually change the value
        }while(randRow == b[randCol]);

        b[randCol] = randRow;
        return new Board(b);
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int b: board){
            if(b < 10){
               sb.append(" ");
            }
            sb.append(b);
            sb.append(" ");
        }
        return sb.toString();
    }
}
