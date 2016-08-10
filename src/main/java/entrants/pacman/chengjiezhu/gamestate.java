package entrants.pacman.chengjiezhu;

import java.util.Queue;

import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class gamestate {
	Game game;
	Queue<MOVE> moves;
	public gamestate(Game game,Queue<MOVE> moves) 
	{
	this.game = game;
	this.moves = moves;
	}
}
