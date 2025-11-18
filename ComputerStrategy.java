package jonin;

import java.util.*;

public class ComputerStrategy implements PlayerStrategy {
    private final Random rand = new Random();
    @Override
    public Move chooseMove(AbstractSOSGame game, AbstractSOSGame.Player player) {
        int n = game.getBoardSize();
        List<Move> opts = new ArrayList<>();
        for (int r=0; r<n; r++) for (int c=0; c<n; c++) {
            if (game.getCell(r,c)==' ') {
                opts.add(new Move(r,c,'S'));
                opts.add(new Move(r,c,'O'));
            }
        }
        return opts.get(rand.nextInt(opts.size()));
    }
}
