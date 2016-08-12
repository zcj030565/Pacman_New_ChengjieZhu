package entrants.pacman.chengjiezhu;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;
import pacman.controllers.Controller;
import pacman.controllers.PacmanController;
import pacman.game.Game;
import java.util.*;

public class Adversarial extends PacmanController{
        //Implement minmax serach
	//Saves possible moves
	private static Queue<MOVE> possiblemove = new LinkedList<MOVE>();
	private static final int maxdepth =2; 
    //An agent selects a move at each state by examining its alternatives via a state evaluation function.
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
		
	public static double evaluation(fullstate pacmannode)
	{
	double hvalue = 0;
	double pilldistance = pacmannode.game.getDistance(pacmannode.game.getPacmanCurrentNodeIndex(), getnearestactivepill(pacmannode.game),DM.PATH);
	pilldistance = 5000/(pilldistance+2);// Nearer Pill Distance Better
	hvalue += pilldistance;
	double powerdistance = pacmannode.game.getDistance(pacmannode.game.getPacmanCurrentNodeIndex(), getnearestactivepower(pacmannode.game),DM.PATH);
	pilldistance = 50000/(powerdistance+2);// Nearer Distance Better.Eating Power does more good
	hvalue += powerdistance;
	int eatdistance=getClosestEdibleGhost(pacmannode.game);
	eatdistance = 2000000/(eatdistance+2);//Nearer edible ghost Distance Better
	hvalue += eatdistance;
	double currentvalue = pacmannode.game.getScore();
	currentvalue=currentvalue;
	hvalue += currentvalue;
	int ghostdistance = getNearestGhostDistance(pacmannode.game);
	//System.out.println(ghostdistance);
	if (ghostdistance==-1)//When Cannot find any ghost, default values is -1
	{
	hvalue += 12500;
	}
	else if (ghostdistance<50)//less than 50 means Pacman is dangerous
	{
	ghostdistance=250*ghostdistance;
    hvalue += ghostdistance;
	}
	else
	{
	hvalue += 12500;	
	}
	return hvalue;
	}

	private static fullstate Adversarial(final fullstate pacmannode,int depth,boolean maxmin, GHOST g, fullstate best) 
	{
		/*
		 * pacmanmnode: Current pacman game state
		 * depth: Maximum search depth available.
		 * maxmin: show current player(maximizer or minimizer)
		 * best: Best state to go
		 * function minimax(node, depth, maximizingPlayer)
			if depth = 0 or node is a terminal node
			return the heuristic value of node
			
			if maximizingPlayer
			bestValue := −∞
			for each child of node
			v := minimax(child, depth − 1, FALSE)
			bestValue := max(bestValue, v)
			return bestValue
			
			else    (* minimizing player *)
			bestValue := +∞
			for each child of node
			v := minimax(child, depth − 1, TRUE)
			bestValue := min(bestValue, v)
			return bestValue*/
		//System.out.println(depth);
		if (depth == 0 || pacmannode.game.gameOver())
		{	
			best = pacmannode;
			best.updatescore(evaluation(best));
			return best;
		    //if depth = 0 or node is a terminal node
			//return the heuristic value of node
		}
	if (maxmin) {
			double a = Integer.MIN_VALUE;
			Stack<fullstate> next = pacmansuccessor(pacmannode);
			GHOST[] ghosts = { GHOST.SUE, GHOST.BLINKY, GHOST.INKY, GHOST.PINKY };
			GHOST nearghost = GHOST.SUE; // Just an initial value
			for (GHOST ghost : ghosts) {
				for (fullstate pacmans : next) {
					fullstate beststate = Adversarial(pacmans, depth - 1, false, ghost, best);
					if (beststate.score > a) {
						a = beststate.score;
						best = beststate;
					}
				}
			}
		}
		else{ 
			double b = Integer.MAX_VALUE;
			Stack<fullstate> next = ghostsuccessor(pacmannode,g);
			for(fullstate ghosts : next)
				{		
				fullstate beststate = Adversarial(ghosts,depth-1,true,null,best);
					if(beststate.score < b)
					{
		            b=beststate.score;
					best=ghosts;
					}
				}
			//return min for ghosts
			}
	//System.out.println(best.moves);	
	return best;	
	}

	public MOVE getMove(Game game, long timeDue) {
	    //Getting next move according to "current game state " then return the next move.
		int current = game.getPacmanCurrentNodeIndex();
		fullstate pacmannode = new fullstate(game, new LinkedList<MOVE>(),0);
		//GHOST[] ghosts = {GHOST.SUE,GHOST.BLINKY,GHOST.INKY,GHOST.PINKY};
		//for(GHOST g:ghosts)		
			fullstate solution = Adversarial(pacmannode, maxdepth, true, null, pacmannode);
			possiblemove = new LinkedList<MOVE>(solution.moves);	
            //System.out.println(possiblemove.size());   
        if  (possiblemove.size()>0)
		    {
		    	return possiblemove.poll();//Get a move to go
		    }
		else{
	        	 Random random = new Random();
			 MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
			 return moves[random.nextInt(moves.length)];
		     }
	         		
	}      
	
	private static Stack<fullstate> pacmansuccessor(fullstate pacmannew)
		{
		    //Using last-in-first-out (LIFO) stack for latest fullstate first		
		    Stack<fullstate> pacmansuccessors = new Stack<fullstate>();	
			MOVE[] totalaction;	
			totalaction = pacmannew.game.getPossibleMoves(pacmannew.game.getPacmanCurrentNodeIndex(), pacmannew.game.getPacmanLastMoveMade()); 	
			//Traverse all of moves from totalaction
			for(int i=0;i<totalaction.length;i++)
				{
				//System.out.println(pacmannew.depth);
				fullstate successor = null;
				Game middle = pacmannew.game.copy();//Middle State copy this fullstate
				Queue<MOVE> pacmanmoves = new LinkedList<MOVE>();
				middle.updatePacMan(totalaction[i]);//Update accroding to Move
				middle.updateGame();
				//Put moves of the fullstate into a Queue
				pacmanmoves.add(totalaction[i]);
				//Create a new game state
				successor = new fullstate(middle,pacmanmoves,pacmannew.depth+1);
				pacmansuccessors.push(successor);			
				}
			return pacmansuccessors;
		}
	
    private static Stack<fullstate> ghostsuccessor(fullstate pacmangnode,GHOST g)
	{
    //Using last-in-first-out (LIFO) stack for dealing with latest fullstate first		
    	Stack<fullstate> ghostsuccessors = new Stack<fullstate>();
	fullstate ghostnext = null;
	MOVE[] ghostaction;
	ghostaction = pacmangnode.game.getPossibleMoves(pacmangnode.game.getGhostCurrentNodeIndex(g),pacmangnode.game.getGhostLastMoveMade(g)); 
		for(int i=0;i<ghostaction.length;i++)				
		{
			//System.out.println(pacmangnode.depth);
			Queue<MOVE> ghostmoves = new LinkedList<MOVE>(pacmangnode.moves);
			Game middle = pacmangnode.game.copy();
		    //HashMap gmoves = new HashMap();
		    //gmoves.put(g, ghostaction[i]);
			//Implement EnumMap to meet the requirement of updateGhosts
			EnumMap<GHOST, MOVE> gmoves = new EnumMap<GHOST, MOVE>(GHOST.class);
		    gmoves.put(g, ghostaction[i]);//Put object and value in map
			middle.updateGhosts(gmoves);
			middle.updateGame();
			ghostnext = new fullstate(middle,ghostmoves,pacmangnode.depth+1);
			ghostsuccessors.push(ghostnext);		
		}
    //System.out.println(ghostsuccessors.size());   
	return ghostsuccessors;
	}   
}
