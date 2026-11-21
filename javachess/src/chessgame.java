
import pieces.*;
import java.util.List;
import java.util.ArrayList;

public class chessgame {
    private chessboard board;
    private boolean whiteTurn = true;
    private position selectedPosition;

    public chessgame() {
        this.board = new chessboard();
    }

    public boolean makeMove(position start, position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? piececolor.WHITE : piececolor.BLACK)) {
            return false; //no piece on position or not your turn
        }

        if (movingPiece.isValidMove(end, board.getBoard()) &&
         !wouldBeCheck(movingPiece.getColor(), start, end)) {
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

    public List<position> getLegalMovesForPieceAt(position position) {
        Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
        if (selectedPiece == null) return new ArrayList<>();

        List<position> legalMoves = new ArrayList<>();
        switch (selectedPiece.getClass().getSimpleName()) {
            case "pawn":
                addPawnMoves(position, selectedPiece.getColor(), legalMoves);
                break;
            case "rook":
                addLineMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}, legalMoves);
                break;
            case "knight":
                addSingleMoves(position, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}}, legalMoves);
                break;
            case "bishop":
                addLineMoves(position, new int[][]{{1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
            case "queen":
                addLineMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
            case "king":
                addSingleMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
        }
      return legalMoves;
    }

    private void addLineMoves(position position, int[][] directions, List<position> legalMoves) {
        for (int[] d : directions) {
            position newPos = new position(position.getRow() + d[0], position.getColumn() + d[1]);
            while (isPositionOnBoard(newPos)) {
                if (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) { //no piece
                    if (!wouldBeCheck((board.getPiece(position.getRow(), position.getColumn()).getColor()), position, newPos)) {
                        legalMoves.add(new position(newPos.getRow(), newPos.getColumn()));
                    }
                    newPos = new position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
                } else {
                    if (board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board.getPiece(position.getRow(), position.getColumn()).getColor()) { //opposing piece
                        if (!wouldBeCheck((board.getPiece(position.getRow(), position.getColumn()).getColor()), position, newPos)) {
                            legalMoves.add(newPos);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void addSingleMoves(position position, int[][] moves, List<position> legalMoves) {
        for (int[] move : moves) {
            position newPos = new position(position.getRow() + move[0], position.getColumn() + move[1]);
            if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getRow(), newPos.getColumn()) == null || board.getPiece(newPos.getRow(), newPos.getColumn()).getColor()
             != board.getPiece(position.getRow(), position.getColumn()).getColor())) { //if valid space and no/enemy piece
                if (!wouldBeCheck((board.getPiece(position.getRow(), position.getColumn()).getColor()), position, newPos)) {
                    legalMoves.add(newPos);
                }   
            }
        }

    }

    private void addPawnMoves(position position, piececolor color, List<position> legalMoves) {
        int direction = color == piececolor.WHITE ? -1 : 1;
        position newPos = new position(position.getRow() + direction, position.getColumn());
        if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null) { //normal single move
            if (!wouldBeCheck((board.getPiece(position.getRow(), position.getColumn()).getColor()), position, newPos)) {
                legalMoves.add(newPos);
            }
        }

        if ((color ==piececolor.WHITE && position.getRow() == 6) || (color == piececolor.BLACK && position.getRow() == 1)) { //check if on starting color's respective square 
            newPos = new position(position.getRow() + 2 * direction, position.getColumn());
            position intermediatePos = new position(position.getRow() + direction, position.getColumn());
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null
             && board.getPiece(intermediatePos.getRow(), intermediatePos.getColumn()) == null) { //check if both squares in front are empty
                if (!wouldBeCheck((board.getPiece(position.getRow(), position.getColumn()).getColor()), position, newPos)) {
                    legalMoves.add(newPos);
                }
            }
        }
        int[] captureCols = {position.getColumn() - 1, position.getColumn() + 1}; //left and right column
        for (int col : captureCols) { //each column
            newPos = new position(position.getRow() + direction, col); //diagonal
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) != null 
             && board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != color) { //if enemy piece
                if (!wouldBeCheck((board.getPiece(position.getRow(), position.getColumn()).getColor()), position, newPos)) {
                    legalMoves.add(newPos);
                }
             }
        }

    }

}