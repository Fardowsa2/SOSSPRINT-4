package jonin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComputerOpponentTest {
    @Test
    public void simulateSimpleGameComputerVsComputer() {
        SimpleSOSGame g = new SimpleSOSGame(5);
        PlayerStrategy ai = new ComputerStrategy();
        while (!g.isGameOver()) {
            Move m = ai.chooseMove(g, g.getCurrentPlayer());
            assertTrue(g.placeLetter(m.getRow(), m.getCol(), m.getLetter()));
        }
        assertTrue(g.isGameOver());
    }

    @Test
    public void simulateGeneralGameComputerVsComputer() {
        GeneralSOSGame g = new GeneralSOSGame(5);
        PlayerStrategy ai = new ComputerStrategy();
        while (!g.isGameOver()) {
            Move m = ai.chooseMove(g, g.getCurrentPlayer());
            assertTrue(g.placeLetter(m.getRow(), m.getCol(), m.getLetter()));
        }
        assertTrue(g.isGameOver());
    }
}
