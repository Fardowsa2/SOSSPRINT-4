package jonin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleSOSGameTest {
    @Test
    public void testSimpleGameWin() {
        SimpleSOSGame g = new SimpleSOSGame(3);
        assertTrue(g.placeLetter(0, 0, 'S')); // Blue
        assertTrue(g.placeLetter(1, 0, 'O')); // Red
        assertTrue(g.placeLetter(0, 1, 'O')); // Blue
        assertTrue(g.placeLetter(2, 2, 'S')); // Red
        assertTrue(g.placeLetter(0, 2, 'S')); // Blue — forms SOS

        assertTrue(g.isGameOver());
        assertEquals(AbstractSOSGame.Player.BLUE, g.getWinner());
    }

    @Test
    public void testSimpleGameDraw() {
        SimpleSOSGame g = new SimpleSOSGame(3);
        // Fill board with no SOS
        g.placeLetter(0, 0, 'O');
        g.placeLetter(0, 1, 'O');
        g.placeLetter(0, 2, 'S');
        g.placeLetter(1, 0, 'S');
        g.placeLetter(1, 1, 'S');
        g.placeLetter(1, 2, 'O');
        g.placeLetter(2, 0, 'O');
        g.placeLetter(2, 1, 'S');
        g.placeLetter(2, 2, 'O');

        assertTrue(g.isGameOver());
        assertNull(g.getWinner());
    }
}
