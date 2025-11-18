package jonin;

public interface PlayerStrategy {
    Move chooseMove(AbstractSOSGame game, AbstractSOSGame.Player player);
}
