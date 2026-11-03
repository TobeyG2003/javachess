package pieces;
public class pawn extends Piece {
    public pawn(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Posititon newPosition, Piece[][] board) {
        int forwardDirection = color == PieceColor.WHITE ? -1 : 1;
        int rowDiff = (newPosition.getRow() - position.getRow()) * forwardDirection;
        int colDiff = (newPosition.getColumn() - position.getColumn();

        //move forward 1 space
        if (colDiff == 0 && rowDiff == 1 && board[newPosition.getRow()][newPosition.getColumn()] == null) {
            return true;
        }

        boolean isStartingPosition = (color == PieceColor.WHITE && position.getRow() == 6) ||
            (color == PieceColor.BLACK && position.getRow() == 1)
    }
}
