import pieces.position;
import pieces.Piece;
import pieces.piececolor;

public class chessgame {
    private chessboard board;
    private boolean whiteTurn = true;

    public chessgame() {
        this.board = new chessboard();
    }

    public boolean makeMove(position start, position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? piececolor.WHITE : piececolor.BLACK)) {
            return false; //no piece on position or not your turn
        }

        if (movingPiece.isValidMove(end, board.getBoard())) {
            board.movePiece(start, end);
            whiteTurn =  !whiteTurn; //switch turn
            return true;
        }
        return false;
    }
}