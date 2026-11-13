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

    public boolean inCheck(piececolor color) {
        position kingPosition = findKingPosition(color);
        for (int row = 0; row < board.getBoard().length; row++) { //check every space on board
            for (int column = 0;  column < board.getBoard()[row].length; column++)  {
                Piece piece = board.getPiece(row, column)
                if (piece != null && piece.getColor() != color) { //if piece is opposite color and can move on king
                    if (piece.isValidMove(kingPosition, board.getBoard())) {
                        return true; //piece is has king in check
                    }
                }
            }
        }
        return false; 
    }
}