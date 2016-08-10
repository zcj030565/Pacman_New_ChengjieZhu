package entrants.pacman.chengjiezhu;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;
import pacman.controllers.Controller;
import pacman.controllers.PacmanController;
import pacman.game.Game;
import java.util.*;

public class BestFirst extends PacmanController{
	//Saves possible moves
	private static Queue<MOVE> possiblemove = new LinkedList<MOVE>(); 
	private static gamestate bestfirst(final gamestate pacmannode, gamestate best) 
	{
	 //pacmanmnode: Current pacman game state
	 //best: Best state to go			
		double a = Integer.MIN_VALUE;
		double most;
		Stack<gamestate> next = pacmanchosenmove(pacmannode);
     //Testing all of current possible move to find the best move
		for(gamestate pacmans : next)
			{
			most = evaluation(pacmans);//Best value
				if(most >= a)
				{
				a = most;
				best=pacmans;//Best State 
				} 
			}	
		return best;
		}	
    private static Stack<gamestate> pacmanchosenmove(gamestate pacmannew)
	{
	    //Using last-in-first-out (LIFO) stack for latest gamestate first		
	    Stack<gamestate> pacmanchosenmoves = new Stack<gamestate>();	
		MOVE[] totalaction;
		totalaction = pacmannew.game.getPossibleMoves(pacmannew.game.getPacmanCurrentNodeIndex(), pacmannew.game.getPacmanLastMoveMade()); 	
		//The size of total action is 1-3.
		//Create State according to all of possible move for evaluation
		for(int i=0;i<totalaction.length;i++)
			{
			gamestate chosenmove = null;
			Game middle = pacmannew.game.copy();//Middle State copy this gamestate
			Queue<MOVE> pacmanmoves = new LinkedList<MOVE>();
			//System.out.println(pacmanmoves);
			middle.updatePacMan(totalaction[i]);//Update accroding to Move
			middle.updateGame();
			//Put moves of the gamestate into a Queue
			pacmanmoves.add(totalaction[i]);
			//Create a new game state
			chosenmove = new gamestate(middle,pacmanmoves);
			pacmanchosenmoves.push(chosenmove);
			}
		return pacmanchosenmoves;
	}
	
    public static double evaluation(gamestate pacmannode)
	{
	double hvalue = 0;
	double pilldistance = pacmannode.game.getDistance(pacmannode.game.getPacmanCurrentNodeIndex(), getnearestactivepill(pacmannode.game),DM.PATH);
	pilldistance = 5000/(pilldistance+2);// Nearer Pill Distance Better
	hvalue += pilldistance;
	double powerdistance = pacmannode.game.getDistance(pacmannode.game.getPacmanCurrentNodeIndex(), getnearestactivepower(pacmannode.game),DM.PATH);
	pilldistance = 50000/(powerdistance+2);// Nearer Distance Better.Eating Power does more good
	hvalue += powerdistance;
	int eatdistance=getClosestEdibleGhost(pacmannode.game);
	eatdistance = 100000/(eatdistance+2);//Nearer edible ghost Distance Better
	hvalue += eatdistance;
	double currentvalue = pacmannode.game.getScore();
	currentvalue=currentvalue;
	hvalue += currentvalue;
	int ghostdistance = getNearestGhostDistance(pacmannode.game);
	//System.out.println(ghostdistance);
	if (ghostdistance==-1)//When Cannot find any ghost, default values is -1
	{
	hvalue += 125000;
	}
	else if (ghostdistance<50)//less than 50 means Pacman is dangerous
	{
	ghostdistance=2500*ghostdistance;
    hvalue += ghostdistance;
	}
	else
	{
	hvalue += 125000;	
	}
	//System.out.println(hvalue);
	return hvalue;
	}
	
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
		return index;
	}
	
	public static int getnearestactivepower(Game game) 
	{
	int[] activePowers = game.getActivePowerPillsIndices();
	int index = 0;
	int distance = Integer.MAX_VALUE;
	int Current = game.getPacmanCurrentNodeIndex();	
		for(int power:activePowers)
			{
			int dis = (int) game.getDistance(Current,power,DM.PATH);
			if(dis<distance)
			{
			distance = dis;
			index = power;//Index of nearest power
			}
			}
		return index;
	}

	public static int getNearestGhostDistance(Game game) 
	//Avoid from nearest ghost
	//If there is edible ghost, return max value.
	{
		int ghostdistance =Integer.MAX_VALUE;
		GHOST[] ghosts = {GHOST.BLINKY,GHOST.INKY,GHOST.PINKY,GHOST.SUE};
		List<GHOST> edibleghosts = new ArrayList<GHOST>();
		for(GHOST ghost:ghosts)
		{
			if(game.getGhostEdibleTime(ghost)>0)
			{
				edibleghosts.add(ghost);
			}
		}		
		if (edibleghosts.size()==0)//There is no edible ghosts
		{
			for(GHOST ghost:ghosts)
			{
				int distance= (int) game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), DM.PATH);
				if(distance<ghostdistance)
				{
					ghostdistance = distance;//Find nearest Ghost
				}
			}	
		}
		//System.out.println(ghostdistance);
		return ghostdistance;
	}
		
	private static int getClosestEdibleGhost(Game game)
		{
		// Find nearest edible ghost and then get distance between current location and the ghost.
	    // If there is no edible ghost return Integer.MAX_VALUE.
			GHOST[] ghosts = {GHOST.BLINKY,GHOST.INKY,GHOST.PINKY,GHOST.SUE};
			List<GHOST> edibleGhosts = new ArrayList<GHOST>();
			for(GHOST ghost:ghosts)
			{
				if(game.getGhostEdibleTime(ghost)>0)
				{
					edibleGhosts.add(ghost);
				}
			}
			int mindis = Integer.MAX_VALUE;
			for(GHOST ghost:edibleGhosts)//Check all of edible Ghosts
			{
				int distance= (int)game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), DM.PATH);
				if(distance<mindis)
				{
					mindis = distance;//nearest
				}
			}
			return mindis;
		}
			
	public MOVE getMove(Game game, long timeDue) 
	{
	    //Getting next move according to "current game state " then return the next move.
		gamestate pacmannode = new gamestate(game, new LinkedList<MOVE>());
	    gamestate solution = bestfirst(pacmannode,pacmannode);
		possiblemove = new LinkedList<MOVE>(solution.moves);
	    if  (possiblemove.size()>0)
	    {
	    	return possiblemove.poll();//Get a move to go
	    }
	    else
	     {
	    	 Random random = new Random();
		 MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		 return moves[random.nextInt(moves.length)];
		 }		
	}
	    
}
