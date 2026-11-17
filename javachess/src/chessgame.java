
import pieces.*;

public class chessgame {
    private chessboard board;
    private boolean whiteTurn = true;
    private position selectedPosition;

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

    public boolean isCheck(piececolor color) {
        position kingPosition = findKingPosition(color);
        for (int row = 0; row < board.getBoard().length; row++) { //check every space on board
            for (int column = 0;  column < board.getBoard()[row].length; column++)  {
                Piece piece = board.getPiece(row, column);
                if (piece != null && piece.getColor() != color) { //if piece is opposite color and can move on king
                    if (piece.isValidMove(kingPosition, board.getBoard())) {
                        return true; //piece is has king in check
                    }
                }
            }
        }
        return false; 
    }

    public boolean isCheckmate(piececolor color) {
        if(!isCheck(color)) {
            return false; //must be in check to checkmate
        }

        position kingPosition = findKingPosition(color);
        king king = (king) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

        for(int rowOffset = -1; rowOffset <= 1; rowOffset++) { //check moves around king
            for (int colOffset = -1; colOffset <=1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) { //ignore current pos
                    continue;
                }
                position newPosition = new position(kingPosition.getRow() + rowOffset, kingPosition.getColumn() + colOffset); //check if there are available positions
                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard()) && !wouldBeCheck(color, kingPosition, newPosition)) {
                    return false; //move exists that escapes any check, not mate
                }
            }
        }

        return true; //no escape
    }

    private boolean wouldBeCheck(piececolor color, position currentPos, position newPosition) {
        Piece temp = board.getPiece(newPosition.getRow(), newPosition.getColumn());
        board.setPiece(newPosition.getRow(), newPosition.getColumn(), board.getPiece(currentPos.getRow(), currentPos.getColumn()));
        board.setPiece(currentPos.getRow(), currentPos.getColumn(), null);

        boolean inCheck = isCheck(color);

        board.setPiece(currentPos.getRow(), currentPos.getColumn(), board.getPiece(newPosition.getRow(), newPosition.getColumn()));
        board.setPiece(newPosition.getRow(), newPosition.getColumn(), temp);

        return inCheck;
    }

    private boolean isPositionOnBoard(position position) {
      return position.getRow() >= 0 && position.getRow() < board.getBoard().length &&
              position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;

    }

    private position findKingPosition(piececolor color) {
        for (int row = 0; row < board.getBoard().length; row++) { //check every space on the board to find king  
          for (int col = 0; col < board.getBoard()[row].length; col++) {
              Piece piece = board.getPiece(row, col);
              if (piece instanceof king && piece.getColor() == color) {
                  return new position(row, col);
              }
          }
      }
      throw new RuntimeException("Error: King not found");
    }

    public chessboard getBoard() {
        return this.board;
    }

    public void resetGame() {
        this.board = new chessboard();
        this.whiteTurn = true;
    }

    public piececolor getCurrentPlayerColor() {
        return whiteTurn ? piececolor.WHITE : piececolor.BLACK;
    }

    public boolean isPieceSelected() {
        return selectedPosition != null;
    }

    public boolean handleSquareSelection(int row, int col) {
        if (selectedPosition == null) {
            Piece selectedPiece = board.getPiece(row, col);
            if (selectedPiece != null && selectedPiece.getColor() == (whiteTurn ? piececolor.WHITE  : piececolor.BLACK)) { //if your own piece was selected
                selectedPosition = new position(row, col);
                return false; //for marking selection but no action
            }
        } else { //selected not null -> piece has already been selected and now is taking action
            boolean moveMade = makeMove(selectedPosition, new position(row, col)); //try to move, return if it was valid
            selectedPosition = null; //reset selection
            return moveMade;
        }
        return false; //no piece, or no move made
    }
}