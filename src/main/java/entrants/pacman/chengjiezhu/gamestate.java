package entrants.pacman.chengjiezhu;

import java.util.Queue;

import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class gamestate {
	Game game;
	Queue<MOVE> moves;
	int depth;
	public gamestate(Game game,Queue<MOVE> moves,int depth) 
	{
	this.game = game;
	this.moves = moves;
	this.depth=depth;
	}
}
