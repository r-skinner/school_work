import java.util.Scanner;

public class Main {
    private static Scanner scanner;
    private static Board board;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        initializeGame();

        while (true){
            gameLoop();
        }
    }

    private static void initializeGame() {
        System.out.println("Who moves first?\n1. Computer\n2. Opponent");

        int firstMove = 0;
        do{
            System.out.print("> ");
            try{
                String input = scanner.next();
                firstMove = Integer.valueOf(input);
                if (firstMove < 1 || firstMove > 2) {
                    System.out.println("Please enter 1 or 2.");
                }
            }
            catch (NumberFormatException e){
                System.out.println("Please enter 1 or 2.");
            }
        }while(firstMove < 1 || firstMove > 2);

        board = new Board(firstMove == 1);

        board.currentMove = new Move(board.board, board.computerPosition, board.opponentPosition);

        board.updateMoveList();

        if(board.computerStarts){
            board.makeComputersMove();
        }
    }

    private static void gameLoop(){
        board.numberOfTurnsCompleted++;
        board.updateMoveList();

        board.printBoard();

        board.makeOpponentsMove();
        board.checkForWinners();

        board.makeComputersMove();
        board.checkForWinners();

        System.gc(); //delete old nodes in the tree
    }

}