import java.util.Collections;
import java.util.Scanner;

class Board {
    private final int SIZE = 8;

    private Scanner scanner;

    byte[] board;

    private String[] moveHistoryList;
    int numberOfTurnsCompleted = 0;

    byte opponentPosition, computerPosition;

    private String player1name, player2name; //for scoreboard output, 'Computer' or 'Opponent'

    boolean computerStarts;

    Move currentMove;

    Board(boolean computerStarts) {
        this.computerStarts = computerStarts;
        scanner = new Scanner(System.in);

        board = new byte[SIZE * SIZE];
        moveHistoryList = new String[SIZE];

        player1name = computerStarts? "Computer" : "Opponent";
        player2name = !computerStarts? "Computer" : "Opponent";
        computerPosition = (byte)(computerStarts ? 0 : SIZE*SIZE-1);
        opponentPosition = (byte)(!computerStarts ? 0 : SIZE*SIZE-1);

        board[computerPosition] = 1;
        board[opponentPosition] = 2;
    }

    void makeOpponentsMove(){
        boolean isMoveValid = false;
        String input;
        do{
            System.out.print("Enter Opponent's move: ");
            input = scanner.next().toUpperCase();

            if(input.matches("^[A-H][1-8]")) {
                isMoveValid = validateOpponentMove(input);
            } else {
                System.out.println("Invalid input. Must be in A1 notation.");
            }

        }while (!isMoveValid);

        moveOpponentTo(A1toCellNumber(input));

        moveHistoryList[(numberOfTurnsCompleted - 1) % SIZE] += input + "  "; // add move to list

        for(Move child : currentMove.getOpponentChildren()){ //find and return the optimal node
            if(child.opponentPosition == opponentPosition) {
                currentMove = child;
            }
        }
    }

    private void moveOpponentTo(byte moveLocation) {
        board[opponentPosition] = -1;
        board[moveLocation] = 2;
        opponentPosition = moveLocation;
    }

    private boolean validateOpponentMove(String input) {
        byte desiredLocation = A1toCellNumber(input);

        // if move is blocked
        if(board[desiredLocation] != 0){
            System.out.println("Invalid move. Blocked Destination.");
            return false;
        }

        byte startingLocation = (desiredLocation < opponentPosition) ? desiredLocation : opponentPosition;
        byte endingLocation = (desiredLocation < opponentPosition) ? opponentPosition : desiredLocation;

        byte startingColumn = (byte)(startingLocation % SIZE);
        byte startingRow = (byte)(startingLocation / SIZE);
        byte endingColumn = (byte)(endingLocation % SIZE);
        byte endingRow = (byte)(endingLocation / SIZE);
        byte incrementAmount;

        if(startingColumn == endingColumn){
            incrementAmount = SIZE;
        }
        else if (startingRow == endingRow){
            incrementAmount = 1;
        }
        else if(Math.abs(startingColumn - endingColumn) - Math.abs(startingRow - endingRow) == 0){
            incrementAmount =  (byte)((startingColumn < endingColumn) ? (SIZE+1) : (SIZE-1));
        }
        else{
            System.out.println("Invalid move. Not a straight line.");
            return false;
        }
        for(byte i = (byte)(startingLocation + incrementAmount); i < endingLocation; i += incrementAmount){ // check in between
            if(board[i] != 0){
                System.out.printf("Invalid move. Blocked path at %s.\n", cellNumberToA1(i));
                return false;
            }
        }
        return true;
    }

    void makeComputersMove(){
        System.out.print("\nComputer is thinking.. ");

        currentMove = alphaBetaSearch(currentMove, (byte) (6 + numberOfTurnsCompleted/3));

        System.out.printf("Computer moves to : %s.\n\n", cellNumberToA1(currentMove.computerPosition));
        moveComputerTo(currentMove.computerPosition);

        moveHistoryList[(numberOfTurnsCompleted - (computerStarts ? 0 : 1)) % SIZE] += cellNumberToA1(currentMove.computerPosition) + "  "; // add move to list
    }

    private void moveComputerTo(byte moveLocation) {
        board[computerPosition] = -1;
        board[moveLocation] = 1;
        computerPosition = moveLocation;
    }

    private static Move alphaBetaSearch(Move move, byte depth){
        maxValue(move, depth, Byte.MIN_VALUE, Byte.MAX_VALUE);

        Collections.shuffle(move.getComputerChildren()); // By shuffling it will let us get a random node with the optimal value
        for(Move child : move.getComputerChildren()){ //find and return the optimal node
            if(child.value == move.value){
                return child;
            }
        }

        System.out.println("\nSOMETHING WENT WRONG..");
        System.exit(-69);
        return null;
    }

    private static short maxValue(Move move, byte depth, byte alpha, byte beta){
        if(depth == 0) return move.getScore();

        move.value = Short.MIN_VALUE;

        for(Move child : move.getComputerChildren()){
            move.value = (short)Math.max(move.value, minValue(child, (byte)(depth -1), alpha, beta));
            if (move.value >= beta) return child.value;
            alpha = (byte) Math.max(move.value, alpha);
        }
        return move.value;
    }

    private static short minValue(Move move, byte depth, byte alpha, byte beta){
        if(depth == 0) return move.getScore();

        move.value = Short.MAX_VALUE;

        for(Move child : move.getOpponentChildren()){
            move.value = (short)Math.min(move.value, maxValue(child, (byte)(depth -1), alpha, beta));
            if(move.value <= alpha) return move.value;
            beta = (byte)Math.min(move.value, beta);
        }

        return move.value;
    }

    //convert cell number to A1 notation. A1 = 0, H8 = 63
    private String cellNumberToA1(byte cellNumber){return "" + ((char)(cellNumber/SIZE + (int)'A')) + ( (cellNumber%SIZE) + 1 );}

    //assumed already checked with regex
    private byte A1toCellNumber(String A1){return (byte)((A1.charAt(0)-(int)'A')*SIZE + (A1.charAt(1)-(int)'1'));}

    void updateMoveList(){ moveHistoryList[(numberOfTurnsCompleted) % SIZE] = numberOfTurnsCompleted + 1 + ". "; }

    void printBoard(){
        System.out.printf("  1 2 3 4 5 6 7 8\t%s vs. %s\n", player1name, player2name);
        for(int rowNumber = 0; rowNumber < SIZE; rowNumber++){
            System.out.print((char)(rowNumber+65) + " ");
            for(int columnNumber = 0; columnNumber < SIZE; columnNumber++){
                int cellValue = board[rowNumber* SIZE + columnNumber];
                switch (cellValue){
                    case -1: System.out.print("# ");
                        break;
                    case 0: System.out.print("- ");
                        break;
                    case 1: System.out.print("X ");
                        break;
                    case 2: System.out.print("O ");
                        break;
                }
            }

            //print out move history
            if(rowNumber < numberOfTurnsCompleted - (computerStarts ? 0 : 1)) {
                System.out.print("\t\t" + moveHistoryList[rowNumber]);
            }

            System.out.println();
        }
    }

    void checkForWinners() {
        if(currentMove.getOpponentChildren().size() == 0){
            System.out.println("COMPUTER WINS");
            System.exit(0);
        }
        if(currentMove.getComputerChildren().size() == 0){
            System.out.println("OPPONENT WINS");
            System.exit(0);
        }
    }
}