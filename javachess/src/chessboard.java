
import pieces.*;

public class chessboard {
    private Piece[][] board;

    public chessboard() {
        this.board = new Piece[8][8];
        setUpPieces();
    }

    private void setUpPieces() {
        // Place Rooks
        board[0][0] = new rook(piececolor.BLACK, new position(0, 0));
        board[0][7] = new rook(piececolor.BLACK, new position(0, 7));
        board[7][0] = new rook(piececolor.WHITE, new position(7, 0));
        board[7][7] = new rook(piececolor.WHITE, new position(7, 7));
        // Place Knights
        board[0][1] = new knight(piececolor.BLACK, new position(0, 1));
        board[0][6] = new knight(piececolor.BLACK, new position(0, 6));
        board[7][1] = new knight(piececolor.WHITE, new position(7, 1));
        board[7][6] = new knight(piececolor.WHITE, new position(7, 6));
        // Place Bishops
        board[0][2] = new bishop(piececolor.BLACK, new position(0, 2));
        board[0][5] = new bishop(piececolor.BLACK, new position(0, 5));
        board[7][2] = new bishop(piececolor.WHITE, new position(7, 2));
        board[7][5] = new bishop(piececolor.WHITE, new position(7, 5));
        // Place Queens
        board[0][3] = new queen(piececolor.BLACK, new position(0, 3));
        board[7][3] = new queen(piececolor.WHITE, new position(7, 3));
        // Place Kings
        board[0][4] = new king(piececolor.BLACK, new position(0, 4));
        board[7][4] = new king(piececolor.WHITE, new position(7, 4));
        // Place Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new pawn(piececolor.BLACK, new position(1, i));
            board[6][i] = new pawn(piececolor.WHITE, new position(6, i));
        }
    }

    public void movePiece(position start, position end) {
        if (board[start.getRow()][start.getColumn()] != null && 
            board[start.getRow()][start.getColumn()].isValidMove(end, board)) { //check if piece start exists + valid

            board[end.getRow()][end.getColumn()] = board[start.getRow()][start.getColumn()]; //place piece at end
            board[end.getRow()][end.getColumn()].setPosition(end); //update position of piece
            board[start.getRow()][start.getColumn()] = null; //remove piece at start
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPiece(int row, int column) {
        return board[row][column];
    }

    public void setPiece(int row, int column, Piece piece) {
        board[row][column] = piece;
        if (piece != null) {
            piece.setPosition(new position(row, column));
        }
    }
}
