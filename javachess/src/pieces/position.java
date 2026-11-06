package pieces;
public class position {
    private int row;
    private int column;

    public position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
}