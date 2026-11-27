import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import pieces.*;

public class chessgamegui extends JFrame {
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private final chessgame game = new chessgame();
    private JTextArea moveHistoryArea;
    private JLabel turnLabel;
    private JLabel whiteCapturesLabel;
    private JLabel blackCapturesLabel;

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

    public chessgamegui() {
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        turnLabel = new JLabel("Turn: White", SwingConstants.CENTER);
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        whiteCapturesLabel = new JLabel("White captures: ");
        blackCapturesLabel = new JLabel("Black captures: ");

        JPanel capturesPanel = new JPanel(new GridLayout(2, 1));
        capturesPanel.add(whiteCapturesLabel);
        capturesPanel.add(blackCapturesLabel);

        topBar.add(turnLabel, BorderLayout.WEST);
        topBar.add(capturesPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(8,8));
        boardPanel.setPreferredSize(new Dimension(512,512));
        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);

        moveHistoryArea = new JTextArea(20,18);
        moveHistoryArea.setEditable(false);
        moveHistoryArea.setFont(new Font("Monospaced", Font.PLAIN,14));

        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Move History"));
        scrollPane.setPreferredSize(new Dimension(200, 512));
        add(scrollPane, BorderLayout.EAST);

        addGameResetOption();
        pack();
        setMinimumSize(new Dimension(712, 560));
        setVisible(true);
    }

    private void initializeBoard(JPanel boardPanel) {
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
                boardPanel.add(square);
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
        boolean moveResult = game.handleSquareSelection(row, col);
        clearHighlights();
        if (moveResult) { //if move went through
            refreshBoard();
            position endPos = new position(row, col);
            if (game.isPromotionMove(endPos)) { //if moved to a promotion row with pawn
                String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                int choiceIndex = JOptionPane.showOptionDialog(
                    this,
                    "Choose a piece for pawn promotion:",
                    "Pawn Promotion",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                String choice = (choiceIndex >= 0) ? options[choiceIndex] : "Queen"; //default option
                game.promotePawn(endPos, choice); //promote to choice
                refreshBoard(); // redraw with the newly promoted piece icon
            }
            updateMoveHistory();
            updateStatusBar();
            checkGameState();
            checkGameOver();
        } else if (game.isPieceSelected()) {
            highlightLegalMoves(new position(row, col));
        }
        refreshBoard();
    }

    private void checkGameState() {
        piececolor currentPlayer = game.getCurrentPlayerColor();
        boolean inCheck = game.isCheck(currentPlayer);

        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check");
        }
    }

    private void highlightLegalMoves(position position) {
        List<position> legalMoves = game.getLegalMovesForPieceAt(position);
        for (position move : legalMoves) {
            squares[move.getRow()][move.getColumn()].setBackground(Color.GREEN);
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? new Color(227,217,207) : new Color(45,60,115));
            }
        }
    }

    private void addGameResetOption() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem resetItem  = new JMenuItem("Reset");
        resetItem.addActionListener(e -> resetGame());
        gameMenu.add(resetItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void resetGame() {
        game.resetGame();
        clearHighlights();
        updateMoveHistory();
        updateStatusBar();
        refreshBoard();
    }

    private void checkGameOver() {
        piececolor currentColor = game.getCurrentPlayerColor();
        String message = null;

        if (game.isCheckmate(currentColor)) {
            String winner = (currentColor == piececolor.WHITE) ? "Black" : "White";
            message = "Checkmate! " + winner + " wins! Play again?";
        } else if (game.isStalemate(currentColor)) {
            message = "Draw by Stalemate! Play again?";
        } else if (game.isInsufficientMaterial()) {
            message = "Draw by Insufficient Material! Play again?";
        } else if (game.isFiftyMoveRule()) {
            message = "Draw by 50-Move Rule! Play again?";
        } else if (game.isThreefoldRepetition()) {
            message = "Draw by Threefold Repetition! Play again?";
        }

        if (message != null) {
            int response = JOptionPane.showConfirmDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    private void updateMoveHistory() {
        List<String> moves = game.getMoveHistory();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i += 2) {
            int moveNumber = (i / 2) + 1;
            String whiteMove = moves.get(i);
            String blackMove = (i + 1 < moves.size()) ? moves.get(i + 1) : "";
            sb.append(String.format("%2d. %-8s %s%n", moveNumber, whiteMove, blackMove));
        }
        moveHistoryArea.setText(sb.toString());
        moveHistoryArea.setCaretPosition(moveHistoryArea.getDocument().getLength());
    }

    private void updateStatusBar() {
        String currentTurn = (game.getCurrentPlayerColor() == piececolor.WHITE) ? "White" : "Black"; //updating turn
        turnLabel.setText("Turn: " + currentTurn);

        //list of icons form captured pieces
        StringBuilder whiteIcons = new StringBuilder();
        for (Piece p : game.getWhiteCapturedPieces()) {
            whiteIcons.append(pieceUnicodeMap.get(p.getClass()));
        }
        StringBuilder blackIcons = new StringBuilder();
        for (Piece p : game.getBlackCapturedPieces()) {
            blackIcons.append(pieceUnicodeMap.get(p.getClass()));
        }

        //material advantage
        int advantage = game.getMaterialAdvantage();
        String whiteAdvText = (advantage > 0) ? " (+" + advantage + ")" : "";
        String blackAdvText = (advantage < 0) ? " (+" + Math.abs(advantage) + ")" : "";

        whiteCapturesLabel.setText("White: " + whiteIcons + whiteAdvText);
        blackCapturesLabel.setText("Black: " + blackIcons + blackAdvText);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(chessgamegui::new);
    }
}
