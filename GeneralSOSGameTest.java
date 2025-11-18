package jonin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneralSOSGameTest {
    @Test
    public void testGeneralGameExtraTurn() {
        GeneralSOSGame g = new GeneralSOSGame(3);
        assertTrue(g.placeLetter(0, 0, 'S')); // Blue
        assertTrue(g.placeLetter(1, 0, 'S')); // Red
        assertTrue(g.placeLetter(0, 1, 'O')); // Blue
        assertTrue(g.placeLetter(2, 0, 'O')); // Red
        assertTrue(g.placeLetter(0, 2, 'S')); // Blue forms SOS

        assertEquals(AbstractSOSGame.Player.BLUE, g.getCurrentPlayer());
        assertTrue(g.getScoreBlue() > 0);
    }

    @Test
    public void testGeneralGameOverAndWinner() {
        GeneralSOSGame g = new GeneralSOSGame(3);
        // Simulate one SOS by Red
        g.placeLetter(0, 0, 'S'); // Blue
        g.placeLetter(0, 1, 'O'); // Red
        g.placeLetter(0, 2, 'O'); // Blue
        g.placeLetter(1, 0, 'S'); // Red
        g.placeLetter(1, 1, 'O'); // Blue
        g.placeLetter(1, 2, 'S'); // Red forms SOS
        // Fill rest
        g.placeLetter(2, 0, 'O');
        g.placeLetter(2, 1, 'O');
        g.placeLetter(2, 2, 'S');

        assertTrue(g.isGameOver());
        String status = g.getGameStatus();
        assertTrue(status.contains("Game over"));
        assertTrue(g.getScoreRed() > g.getScoreBlue());
    }
}
