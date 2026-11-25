package pieces;

public class king extends Piece {

    private boolean hasMoved = false;

    public king(piececolor color, position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(position newPosition, Piece[][] board) {
        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
        int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());
        boolean castle = !hasMoved && rowDiff == 0 && colDiff == 2;
        int castleDir = newPosition.getColumn() - position.getColumn();

        boolean isOneSquare = rowDiff <= 1 && colDiff <=1 && !(rowDiff == 0 && colDiff == 0); //only normally allowed to move one square
        if (!isOneSquare && !castle) { //not a 1 square move (trying to castle) or bad square and castling disallowed)
            return false;
        }

        if (castle) { //castling allowed and attempted
            int row = newPosition.getRow();
            if (castleDir > 0) { //castle right kingside
                if (board[row][7] instanceof rook castleRook && //check for kingside rook
                 board[row][5] == null && board[row][6] == null && //empty spaces
                 !castleRook.checkMoved() && //valid castle rook
                 castleRook.getColor() == this.getColor()) { //same team
                    return true;
                }
            } else { //castle left queenside
                if (board[row][0] instanceof rook castleRook && //check for queenside rook
                 board[row][3] == null && board[row][2] == null && board[row][1] == null && //empty spaces
                 !castleRook.checkMoved() && //valid castle rook
                 castleRook.getColor() == this.getColor()) { //same team
                    return true;
                }
            }
            return false;
        }

        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        return destinationPiece == null || destinationPiece.getColor() != this.getColor(); //check if capture or empty space
    }

    public boolean checkMoved() {
        return hasMoved;
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }
}
