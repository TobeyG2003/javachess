import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ChessSquareComponent extends JButton {
    private int row;
    private int col;

    public ChessSquareComponent(int row, int col) {
        this.row = row;
        this.col = col;
        initButton();
    }

    private void initButton() {
        setPreferredSize(new Dimension(64,64));
        setMinimumSize(new Dimension(32,32));
        setMargin(new Insets(0,0,0,0));

        if ((row+col) % 2 == 0) {
            setBackground(new Color(227,217,207));
        } else {
            setBackground(new Color(45,60,115));
        }

        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);

        setFont(new Font("Segoe UI Symbol", Font.PLAIN, 42));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                int size = Math.min(getWidth(), getHeight());
                int fontSize = Math.max(12, Math.min(42, Math.round(size * 0.68f)));
                setFont(getFont().deriveFont((float) fontSize));
            }
        });
    }

    public void setPieceSymbol(String symbol, Color color) {
        this.setText(symbol);
        this.setForeground(color);
    }

    public void clearPieceSymbol() {
      this.setText("");
    }
}
