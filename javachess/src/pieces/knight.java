package pieces;

public class knight extends Piece {
    public knight(piececolor color, position position) {
        super(color, position);
    }

    @Override 
    public boolean isValidMove(position newPosition, Piece[][] board) {
        if (newPosition.equals(this.position)) {
            return false; //same as current square
        }

        int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());

        boolean isValidL = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2); //check L move

        if (!isValidL) {
            return false;
        }

        Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];
        if (targetPiece == null) {
            return true; //empty space
        } else {
            return targetPiece.getColor() != this.getColor(); //capture
        }
    }
}
