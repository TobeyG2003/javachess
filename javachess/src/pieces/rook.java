package pieces;

import javax.swing.text.Position;

public class rook extends Piece {
    
    public Rook(PieceColor color, Position position) {
        super(color, position);
    }


    @Override 
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        if (position.getRow() == newPosition.getRow())  { //horizontal 
            int columnStart = Math.min(position.getColumn(), newPosition.getColumn()) + 1;
            int columnEnd = Math.max(position.getColumn(), newPosition.getColumn());
            for (int column = columnStart; column < columnEnd; column++) {
                if(board[position.getRow()][column] != null) {
                    return false; //piece in between start and end
                }
            }
        } else if (position.getColumn() == newPosition.getColumn()) { //vertical
            int rowStart = Math.min(position.getRow(), newPosition.getRow()) + 1;
            int rowEnd = Math.max(position.getRow(), newPosition.getRow());
            for (int row = rowStart; row < rowEnd; row++) {
              if (board[row][position.getColumn()] != null) {
                  return false; // piece in between
              }
          }
        } else {
            return false; //not straight
        }

        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        if (destinationPiece == null) {
            return true; // no piece
        } else if (destinationPiece.getColor() != this.getColor()) {
            return true; // enemy piece
        }
        return false; // own piece

    }
}
