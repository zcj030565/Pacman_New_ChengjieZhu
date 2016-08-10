package entrants.pacman.chengjiezhu;

import java.util.Queue;

import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class fullstate 
{
	Game game;
	Queue<MOVE> moves;
	int depth;
	double score;
	public fullstate(Game game,Queue<MOVE> moves,int depth) 
	{
	this.game = game;
	this.moves = moves;
	this.depth=depth;
	}
	 public void updatescore(double score) {
	        this.score = score;
	    }

}
