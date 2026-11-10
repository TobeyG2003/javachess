package pieces;

public class bishop extends Piece {
    public bishop(piececolor color, position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(position newPosition, Piece[][] board) {
        int rowDiff = Math.abs(position.getRow()-newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        if (rowDiff != colDiff) { //if not diagonal
            return false;
        }

        int rowStep = newPosition.getRow() > position.getRow() ? 1 : -1; //check direction
        int colStep = newPosition.getColumn() > position.getColumn() ? 1 : -1;
        int steps = rowDiff - 1; //number of squares to check

        for (int i = 1; i <= steps; i++) { //check along squares for pieces
            if (board[position.getRow() + i * rowStep][position.getColumn() + i * colStep] != null) {
                return false;
            }
        }

        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        if (destinationPiece == null) {
            return true; //nothing, move
        }
        if (destinationPiece.getColor() != this.getColor()) {
            return true; //capture
        }

        return false; //friendly piece

    }
}
