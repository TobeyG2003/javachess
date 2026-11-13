import javax.swing.text.Position;

import pieces.*;

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

    public boolean isCheck(piececolor color) {
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

    private boolean isPositionOnBoard(position position) {
      return position.getRow() >= 0 && position.getRow() < board.getBoard().length &&
              position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;

    }

    private position findKingPosition(piececolor color) {
        for (int row = 0; row < board.getBoard().length; row++) {
          for (int col = 0; col < board.getBoard()[row].length; col++) {
              Piece piece = board.getPiece(row, col);
              if (piece instanceof king && piece.getColor() == color) {
                  return new position(row, col);
              }
          }
      }
    }
}