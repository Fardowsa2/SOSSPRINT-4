package jonin;


public class Move {
    private final int row, col;
    private final char letter;
    public Move(int row, int col, char letter) {
        this.row = row; this.col = col; this.letter = letter;
    }
    public int getRow()    { return row; }
    public int getCol()    { return col; }
    public char getLetter(){ return letter; }
}
