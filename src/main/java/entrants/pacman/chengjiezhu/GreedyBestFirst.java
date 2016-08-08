package entrants.pacman.chengjiezhu;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.Random;
import java.util.Stack;
import pacman.controllers.PacmanController;


public class GreedyBestFirst extends PacmanController{
	
	public static int getnearestactivepill(Game game) 
	{
	int[] activePills = game.getActivePillsIndices();
	int index = 0;
	int distance = Integer.MAX_VALUE;
	int Current = game.getPacmanCurrentNodeIndex();
		for(int pill:activePills)
		{   
			int dis = (int) game.getDistance(Current, pill,DM.PATH);
			if(dis<distance)
			{
			distance = dis;
			index = pill;//Index of nearest pill
			}
		}
	int[] activePowers = game.getActivePowerPillsIndices();
		for(int power:activePowers)
			{
			int dis = (int) game.getDistance(Current,power,DM.PATH);
			if(dis<2*distance)//Eating Power does more good
			{
			distance = dis;
			index = power;//Index of nearest power
			}
			}
		return index;
	}
 	
	public static MOVE BestFirst(Game game)
	{
		int current=game.getPacmanCurrentNodeIndex();
		int index=getnearestactivepill(game);
		   Stack<State> go = new Stack<State>();
		   State startpoint = new State(index);
		   if(!startpoint.visited)//if this is a unvisited node
			{
			startpoint.visited = true;	
			go.add(startpoint);//Save best Target
			}
			if (go.size()>0)
			{
			MOVE move = game.getNextMoveTowardsTarget(current,go.pop().point, DM.PATH);
			return move;			 
			}
		else{
			if(game.getNeighbour(current, MOVE.LEFT)<0)
			{
		        System.out.println("x");
				return MOVE.RIGHT;
			}
			else if(game.getNeighbour(current, MOVE.UP)<0)
			{
		        System.out.println("x");
				return MOVE.DOWN;
			}
			else if(game.getNeighbour(current, MOVE.RIGHT)<0)
			{
		        System.out.println("x");
				return MOVE.LEFT;
			}
		    else if(game.getNeighbour(current, MOVE.DOWN)<0)
			{
		        System.out.println("x");
		    	return MOVE.UP;
    		}	
			//If there is no best move, then move Randomly
		    else 
			{
		    	 Random random = new Random();
			 System.out.println("x");
			 MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
			 return moves[random.nextInt(moves.length)];
			 }		
			}
	}
	public MOVE getMove(Game game, long timeDue) 
	{
		MOVE move = BestFirst(game);
		return move;
	}
	
}
