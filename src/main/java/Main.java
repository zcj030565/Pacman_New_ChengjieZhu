import examples.commGhosts.POCommGhosts;
import pacman.Executor;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.AggressiveGhosts;
import entrants.pacman.chengjiezhu.*;
public class Main {

    public static void main(String[] args) {
        Executor executor = new Executor(false, true);
        //executor.runGameTimed(new DFS(), new POCommGhosts(50), true);
        //executor.runGameTimed(new GreedyBestFirst(), new POCommGhosts(50), true);
        //executor.runGameTimed(new BFS(), new POCommGhosts(50), true);
        executor.runGameTimed(new Adversarial(), new AggressiveGhosts(), true);
    }
}
