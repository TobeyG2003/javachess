package pieces;

public class queen extends Piece {
    public queen(piececolor color, position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(position newPosition, Piece[][] board) {
        if (newPosition.equals(this.position)) { //check if same position
            return false;
        }
    
        int rowDiff = Math.abs(position.getRow()-newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());


        boolean straight = this.position.getRow() == newPosition.getRow() || this.position.getColumn() == newPosition.getColumn();
        boolean diagonal = rowDiff == colDiff;
        if (!straight && !diagonal) {
            return false; //not valid queen movement
        }

        int rowDirection = Integer.compare(newPosition.getRow(), this.position.getRow()); //get directions
        int colDirection = Integer.compare(newPosition.getColumn(), this.position.getColumn());

        int currentRow = this.position.getRow() + rowDirection;
        int currentCol = this.position.getColumn() + colDirection;
        while (currentRow != newPosition.getRow() || currentCol != newPosition.getColumn()) {
            if (board[currentRow][currentCol] != null) {
                return false; //piece in way
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }
        //check whats at destination
        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        return destinationPiece == null || destinationPiece.getColor() != this.getColor();
    }
}
