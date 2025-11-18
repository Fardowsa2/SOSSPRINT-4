package jonin;

public abstract class AbstractSOSGame {
    public enum Player { BLUE, RED }

    protected int boardSize;
    protected char[][] board;
    protected Player currentPlayer;
    protected boolean gameOver;

    public AbstractSOSGame(int boardSize) {
        this.boardSize = boardSize;
        startNewGame();
    }


    // Clear board, reset turn to BLUE, and reset scores.
    public void startNewGame() {
        board = new char[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                board[r][c] = ' ';
            }
        }
        currentPlayer = Player.BLUE;
        gameOver = false;
        resetScores();
    }


    protected abstract void resetScores();

    public int getBoardSize() { return boardSize; }
    public char getCell(int r, int c) { return board[r][c]; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return gameOver; }
    public abstract String getGameStatus();

  
    public boolean placeLetter(int row, int col, char letter) {
        if (gameOver) return false;
        if (!isValid(row, col) || (letter != 'S' && letter != 'O')) return false;
        if (board[row][col] != ' ') return false;
        board[row][col] = letter;
        processMove(row, col);
        return true;
    }

   
    protected abstract void processMove(int row, int col);

   
    protected int checkForSOS(int row, int col) {
        int count = 0;
        int[] dr = {-1,-1,-1,0,0,1,1,1};
        int[] dc = {-1,0,1,-1,1,-1,0,1};
        for (int i = 0; i < 8; i++) {
            if (board[row][col] == 'S') {
                int r1 = row + dr[i], c1 = col + dc[i];
                int r2 = row + 2*dr[i], c2 = col + 2*dc[i];
                if (isValid(r1,c1) && isValid(r2,c2)
                    && board[r1][c1]=='O' && board[r2][c2]=='S') count++;
            }
            if (board[row][col] == 'O') {
                int r0 = row - dr[i], c0 = col - dc[i];
                int r1 = row + dr[i], c1 = col + dc[i];
                if (isValid(r0,c0) && isValid(r1,c1)
                    && board[r0][c0]=='S' && board[r1][c1]=='S') count++;
            }
            if (board[row][col] == 'S') {
                int r0 = row - 2*dr[i], c0 = col - 2*dc[i];
                int r1 = row - dr[i], c1 = col - dc[i];
                if (isValid(r0,c0) && isValid(r1,c1)
                    && board[r0][c0]=='S' && board[r1][c1]=='O') count++;
            }
        }
        return count;
    }

    protected boolean isValid(int r, int c) {
        return r >= 0 && r < boardSize && c >= 0 && c < boardSize;
    }

    protected boolean isBoardFull() {
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == ' ') return false;
            }
        }
        return true;
    }
}
