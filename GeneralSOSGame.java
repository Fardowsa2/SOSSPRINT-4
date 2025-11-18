package jonin;


public class GeneralSOSGame extends AbstractSOSGame {
    private int scoreBlue, scoreRed;

    public GeneralSOSGame(int boardSize) { super(boardSize); }
    @Override protected void resetScores() {
        scoreBlue = 0; scoreRed = 0;
    }

    @Override
    protected void processMove(int row, int col) {
        int sos = checkForSOS(row, col);
        if (currentPlayer == Player.BLUE) scoreBlue += sos;
        else                               scoreRed  += sos;

        if (isBoardFull()) {
            gameOver = true;
        } else if (sos == 0) {
            // only switch if no SOS formed
            currentPlayer = (currentPlayer == Player.BLUE) ? Player.RED : Player.BLUE;
        }
    }

    @Override
    public String getGameStatus() {
        if (!gameOver)
            return String.format("Ongoing: %s to move. Scores B:%d R:%d",
                                  currentPlayer, scoreBlue, scoreRed);
        if (scoreBlue > scoreRed)
            return String.format("Game over: Blue wins %d to %d", scoreBlue, scoreRed);
        if (scoreRed > scoreBlue)
            return String.format("Game over: Red wins %d to %d", scoreRed, scoreBlue);
        return String.format("Game over: Draw. Score %d-%d", scoreBlue, scoreRed);
    }

    public int getScoreBlue() { return scoreBlue; }
    public int getScoreRed()  { return scoreRed;  }
}
