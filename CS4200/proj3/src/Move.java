import java.util.ArrayList;
import java.util.BitSet;

class Move {
    private static final byte SIZE = 8;
    private final BitSet board;
    final byte computerPosition, opponentPosition;
    short value;

    private ArrayList<Move> computerChildren;
    private ArrayList<Move> opponentChildren;


    private Move(BitSet board, byte position, byte opponentPosition){
        this.board = board;
        this.computerPosition = position;
        this.opponentPosition = opponentPosition;
    }

    Move(byte[] board, byte position, byte opponentPosition){
        this(byteArrayToBitSet(board), position, opponentPosition);
    }

    private static BitSet byteArrayToBitSet(byte[] board) {
        BitSet bitset = new BitSet(64);
        for(byte i = 0; i < 64; i ++) {
            if (board[i] != 0) {
                bitset.set(i);
            }
        }
        return bitset;
    }

    short getScore(){
        byte biasComputerMoves = 2;
        byte biasOpponantMoves = -1;
        byte biasSurroundingMoves = -2;


        return (short)(
                getComputerMoveCount()*biasComputerMoves +
                getOpponentMoveCount()*biasOpponantMoves +
                getSurroundingCount()*biasSurroundingMoves);
    }

    private byte getComputerMoveCount() {
        byte count = 0;
        byte column = (byte)(computerPosition%SIZE);
        for(byte i = (byte)(computerPosition-8); i >= 0;                    i -= 8){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(computerPosition-7); i >= 0 && i%SIZE > column; i -= 7){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(computerPosition+1); i < 64 && i%SIZE > column; i += 1){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(computerPosition+9); i < 64 && i%SIZE > column; i += 9){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(computerPosition+8); i < 64;                    i += 8){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(computerPosition+7); i < 64 && i%SIZE < column; i += 7){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(computerPosition-1); i >= 0 && i%SIZE < column; i -= 1){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(computerPosition-9); i >= 0 && i%SIZE < column; i -= 9){
            if(board.get(i)) break;
            count ++;
        }
        return count;
    }

    private byte getOpponentMoveCount() {
        byte count = 0;
        byte column = (byte)(opponentPosition%SIZE);
        for(byte i = (byte)(opponentPosition-8); i >= 0;                    i -= 8){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(opponentPosition-7); i >= 0 && i%SIZE > column; i -= 7){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(opponentPosition+1); i < 64 && i%SIZE > column; i += 1){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(opponentPosition+9); i < 64 && i%SIZE > column; i += 9){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(opponentPosition+8); i < 64;                    i += 8){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(opponentPosition+7); i < 64 && i%SIZE < column; i += 7){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(opponentPosition-1); i >= 0 && i%SIZE < column; i -= 1){
            if(board.get(i)) break;
            count ++;
        }
        for(byte i = (byte)(opponentPosition-9); i >= 0 && i%SIZE < column; i -= 9){
            if(board.get(i)) break;
            count ++;
        }
        return count;
    }

    private byte getSurroundingCount() {
        byte count = 0;
        byte column = (byte)(computerPosition%SIZE);

        byte position = (byte)(computerPosition-8);
        if(position >= 0 && !board.get(position)) count++;

        position = (byte)(computerPosition-7);
        if(position >= 0 && position%SIZE > column && !board.get(position)) count++;

        position = (byte)(computerPosition+1);
        if(position < 64 && position%SIZE > column && !board.get(position)) count++;

        position = (byte)(computerPosition+9);
        if(position < 64 && position%SIZE > column && !board.get(position)) count++;

        position = (byte)(computerPosition+8);
        if(position < 64 && !board.get(position)) count++;

        position = (byte)(computerPosition+7);
        if(position < 64 && position%SIZE < column && !board.get(position)) count++;

        position = (byte)(computerPosition-1);
        if(position >= 0 && position%SIZE < column && !board.get(position)) count++;

        position = (byte)(computerPosition-9);
        if(position >= 0 && position%SIZE < column && !board.get(position)) count++;

        return count;
    }

    ArrayList<Move> getComputerChildren() {
        if(computerChildren == null) {
            computerChildren = new ArrayList<>(27);
            byte column = (byte)(computerPosition%SIZE);

            for(byte i = (byte)(computerPosition-8); i >= 0;                    i -= 8){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
            for(byte i = (byte)(computerPosition-7); i >= 0 && i%SIZE > column; i -= 7){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
            for(byte i = (byte)(computerPosition+1); i < 64 && i%SIZE > column; i += 1){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
            for(byte i = (byte)(computerPosition+9); i < 64 && i%SIZE > column; i += 9){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
            for(byte i = (byte)(computerPosition+8); i < 64;                    i += 8){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
            for(byte i = (byte)(computerPosition+7); i < 64 && i%SIZE < column; i += 7){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
            for(byte i = (byte)(computerPosition-1); i >= 0 && i%SIZE < column; i -= 1){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
            for(byte i = (byte)(computerPosition-9); i >= 0 && i%SIZE < column; i -= 9){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                computerChildren.add(new Move(newBoard, i, opponentPosition));
            }
        }
        return computerChildren;
    }

    ArrayList<Move> getOpponentChildren() {
        if(opponentChildren == null) {
            opponentChildren = new ArrayList<>(27);
            byte column = (byte)(opponentPosition% SIZE);

            for(byte i = (byte)(opponentPosition-8); i >= 0;                    i -= 8){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
            for(byte i = (byte)(opponentPosition-7); i >= 0 && i%SIZE > column; i -= 7){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
            for(byte i = (byte)(opponentPosition+1); i < 64 && i%SIZE > column; i += 1){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
            for(byte i = (byte)(opponentPosition+9); i < 64 && i%SIZE > column; i += 9){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
            for(byte i = (byte)(opponentPosition+8); i < 64;                    i += 8){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
            for(byte i = (byte)(opponentPosition+7); i < 64 && i%SIZE < column; i += 7){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
            for(byte i = (byte)(opponentPosition-1); i >= 0 && i%SIZE < column; i -= 1){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
            for(byte i = (byte)(opponentPosition-9); i >= 0 && i%SIZE < column; i -= 9){
                if(board.get(i)) break;
                BitSet newBoard = (BitSet) board.clone();
                newBoard.set(i);
                opponentChildren.add(new Move(newBoard, computerPosition, i));
            }
        }
        return opponentChildren;
    }
}
