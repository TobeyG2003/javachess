package pieces;

public abstract class Piece {
    protected position position;
    protected piececolor color;

    public Piece(piececolor color, position position) {
        this.color = color;
        this.position = position;
    }

    public piececolor getColor() {
        return color;
    }

    public position getPosition() {
        return position;
    }

    public void setPosition(position position) {
        this.position = position;
    }

    public abstract boolean isValidMove(position newPosition, Piece[][] board);
}