
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;

public class Driver {

    public static void main(String[] args){
	Scanner scanner = new Scanner(System.in);

    int response;

	do {
        System.out.printf("[1] Random input%n[2] Enter input%nPlease Choose: ");
        response = scanner.nextInt();
    }while( response != 1 && response != 2);

    Board board;
    Graph ham;
    Graph man;


	switch (response) {
        case 1:
            board = genRandomBoard();
            break;
        case 2:
            board = getUserBoard(scanner);
            break;

            default:
                board = new Board(new int[]{0,1,2,3,4,5,6,7,8});
        }

        ham = new Graph(new Node(0, board, 1, null));
        ham.solve();

        man = new Graph(new Node(0, board, 2, null));
        man.solve();
    }

    private static boolean checkValidity(LinkedList<Integer> linkedList) {
        int errors = 0;
        for (int i = 0; i < linkedList.size(); i++)
            for (int j = i; j < linkedList.size(); j++)
                if (linkedList.get(i) > linkedList.get(j) && linkedList.get(i) > 1 && linkedList.get(j) != 0)
                    errors++;
        return (errors%2==0);
    }

    private static Board genRandomBoard() {

        LinkedList<Integer> temp = new LinkedList<>();
        for (int j = 0; j < 9; j++) {
            temp.add(j);
        }

        do {
            Collections.shuffle(temp);
        } while (!checkValidity(temp));

        return new Board(temp);
    }

    private static Board getUserBoard(Scanner scanner) {

        System.out.println("Please enter valid values:");
        LinkedList<Integer> set = new LinkedList<>();
        int in;
        while(set.size() != 9) {
            in = scanner.nextInt();
            if (in > 8 || in < 0) {
                System.out.printf("%d not between 0 and 8 (inclusive)", in);
            } else if (set.contains(in)) {
                System.out.printf("%d already entered.", in); }
            else {
                set.addLast(in);
            }
        }
        if(!checkValidity(set)) {
            System.out.println("Invalid board configuration.");
            return getUserBoard(scanner);
        }

        return new Board(set);
}

    static class Board{
    private int[] board;
    Board(LinkedList<Integer> linkedList){
        this.board = linkedList.stream().mapToInt(i->i).toArray();
    }

    private Board(int[] board){
        this.board = board;
    }

    int getHamming(){
        int score = 0;
        for(int i = 1; i < board.length; i ++){
            if(i != board[i]){
                score++;
            }
        }
        return score;
    }

    int getManhattan(){
        int indexDiff, distance = 0;
        for(int i = 0; i < board.length; i ++){
            indexDiff = Math.abs(board[i] - i);
            distance += indexDiff/3 + indexDiff%3;
        }
        return distance;
    }

    private int[] getBoard(){
        return board;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i : board){
            s.append(i);
            s.append(" ");
        }
        return s.toString();
    }

    Board[] generateMoves(){
        int indexOfBlankTile = -1;

        for(int i = 0; i < board.length; i ++){
            if( board[i] == 0){
                indexOfBlankTile = i;
                break;
            }
        }

        int rowNumber = indexOfBlankTile/3;
        int colNumber = indexOfBlankTile%3;

        int numOfMoves = ((rowNumber == 1) ? 2 : 1) + ((colNumber == 1) ? 2 : 1);
        
        Board[] boards = new Board[numOfMoves];
        int arrayIndex = 0;

        //top-down
        if(rowNumber >= 1) {
            Board b = new Board(board.clone());
            b.swap(indexOfBlankTile, indexOfBlankTile-3);
            boards[arrayIndex++] = b;
        }

        //bottom-up
        if(rowNumber <= 1) {
            Board b = new Board(board.clone());
            b.swap(indexOfBlankTile, indexOfBlankTile+3);
            boards[arrayIndex++] = b;
        }

        //left-right
        if(colNumber >= 1) {
            Board b = new Board(board.clone());
            b.swap(indexOfBlankTile, indexOfBlankTile-1);
            boards[arrayIndex++] = b;
        }

        //right-left
        if(colNumber <= 1) {
            Board b = new Board(board.clone());
            b.swap(indexOfBlankTile, indexOfBlankTile+1);
            boards[arrayIndex] = b;
        }

        return boards;
    }

    private void swap(int pos1, int pos2){
        board[pos1] = board[pos1] ^ board[pos2];
        board[pos2] = board[pos1] ^ board[pos2];
        board[pos1] = board[pos1] ^ board[pos2];
    }

    @Override
    public boolean equals(Object o) {
        for(int i = 0; i < board.length; i++){
            if(board[i] != (((Board)o).getBoard())[i])
                return false;
        }
        return true;
    }

}

    static class Node{
    private int g; // depth
    private int h; // score
    private int f; // f = g+h
    private int heuristic;
    private Board data;
    private Node parent;

    Node(int g, Board data, int heuristic, Node parent){ // {1:hamming, 2: manhattan}
        this.g = g; //depth
        this.h = (heuristic == 1) ? data.getHamming() : data.getManhattan();
        this.f = g+h;
        this.heuristic = heuristic;
        this.data = data;
        this.parent = parent;
    }

    Node[] generateChildren(){
        Board[] boards = data.generateMoves();
        Node[] c = new Node[boards.length];
        for(int i = 0; i < boards.length; i++){
            c[i] = new Node( g+1,  boards[i], heuristic, this);
            }
        return c;
    }

    int getF() {
        return f;
    }
    int getH() {
        return h;
    }
        Board getData() {
            return data;
        }

        Node getParent() {
            return parent;
        }

    @Override
    public boolean equals(Object o) {
        return this.data.equals(((Node)o).getData());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);
        int[] arr = data.board;
        formatter.format("%d %d %d\n%d %d %d\n%d %d %d\n",
                arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7],arr[8]);

        return stringBuilder.toString();
    }
}

    static class Graph{
    private Node head;
    private int nodesGenerated = 1;
    private LinkedList<Node> frontier;
    private LinkedList<Node> explored;
    private LinkedList<Node> path;
    Graph(Node head){
        this.head = head;
        frontier = new LinkedList<>();
        explored = new LinkedList<>();

    }
    double[] solve(){
        Node currentNode = head;
        long startTime = System.nanoTime();
        while(currentNode.getH() != 0){
            Node[] children = currentNode.generateChildren();
            for (Node n : children) {
                if(!explored.contains(n)) {
                    frontier.add(n);
                    nodesGenerated++;
                }
            }

            frontier.sort(new Comparator<>() {
                @Override
                public int compare(Node o1, Node o2)  {
                    return o1.getF() - o2.getF();
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            });

            explored.add(currentNode);
            currentNode = frontier.pop();
        }
        long endTime = System.nanoTime();
        double time = (endTime-startTime)/1e6;

        path = new LinkedList<>();
        Node temp = currentNode;
        while(temp.getParent() != null){
            path.addFirst(temp);
            temp = temp.getParent();
        }

        System.out.printf("Using h%d, it took %.2f milliseconds and %d nodes to find a solution of %d moves%n",
                currentNode.heuristic, time, nodesGenerated, currentNode.getF());
        if(currentNode.heuristic == 2){            for(Node n: path){
                System.out.println(n);
            }
        }
        return new double[]{(double)currentNode.getF(), (double) nodesGenerated, time};
    }
    }
}