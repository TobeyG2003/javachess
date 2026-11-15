import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import pieces.*;

public class chessgamegui extends JFrame {
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private final chessgame game = new chessgame();

    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() { //map to assign classes an icon
        {
            put(pawn.class, "\u265F"); //unicode for icons
            put(rook.class, "\u265C");
            put(knight.class, "\u265E");
            put(bishop.class, "\u265D");
            put(queen.class, "\u265B");
            put(king.class, "\u265A");  
        }
    };

    public chessgameui() {
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8,8));
        initializeBoard();
        pack();
        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square  = new  ChessSquareComponent(row, col);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }

                });
                add(square);
                squares[row][col] = square;
            }
        }
        refreshBoard();
    }

    private void refreshBoard() {
        chessboard board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    String symbol = pieceUnicodeMap.get(piece.getClass());
                    Color color = (piece.getColor() == piececolor.WHITE) ? Color.WHITE : Color.BLACK;
                    squares[row][col].setPieceSymbol(symbol, color);
                } else {
                    squares[row][col].clearPieceSymbol();
                }
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        if(game.handleSquareSelection(row, col)) {
            refreshBoard();
            checkGameState();
        }
    }

    private void checkGameState() {
        piececolor currentPlayer = game.getCurrentPlayerColor();
        boolean inCheck = game.isCheck(currentPlayer);

        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + "is in check");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(chessgamegui::new);
    }
}
