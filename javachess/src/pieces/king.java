package pieces;

public class king extends Piece {
    public king(piececolor color, position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(position newPosition, Piece[][] board) {
        int rowDiff = Math.abs(position.getRow()-newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        boolean isOneSquare = rowDiff <= 1 && colDiff <=1 && !(rowDiff == 0 && colDiff == 0); //only allowed to move one square
        if (!isOneSquare) {
            return false;
        }

        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        return destinationPiece == null || destinationPiece.getColor() != this.getColor(); //check if capture or empty space
    }
}
