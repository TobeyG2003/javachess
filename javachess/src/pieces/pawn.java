package pieces;

public class pawn extends Piece {
    public pawn(piececolor color, position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(position newPosition, Piece[][] board) {
        int forwardDirection = color == piececolor.WHITE ? -1 : 1;
        int rowDiff = (newPosition.getRow() - position.getRow()) * forwardDirection;
        int colDiff = (newPosition.getColumn() - position.getColumn());

        //move forward 1 space
        if (colDiff == 0 && rowDiff == 1 && board[newPosition.getRow()][newPosition.getColumn()] == null) {
            return true;
        }

        boolean isStartingPosition = (color == piececolor.WHITE && position.getRow() == 6) ||
            (color == piececolor.BLACK && position.getRow() == 1); //check if starting square based on color
        if (colDiff == 0 && rowDiff == 2 && isStartingPosition 
            && board[newPosition.getRow()][newPosition.getColumn()] == null) { //check empty space
                int middleRow = position.getRow() + forwardDirection;
                if (board[middleRow][position.getColumn()] == null) { //check space in front
                    return true; //allow move 2 spaces
                }
        }
        
        if (Math.abs(colDiff) == 1 && rowDiff == 1 && board[newPosition.getRow()][newPosition.getColumn()] != null && //check capture
            board[newPosition.getRow()][newPosition.getColumn()].color != this.color) {
                return true;
        }

        return false;
    }
}
