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
	private static Queue<MOVE> possiblemove = new LinkedList<MOVE>(); 
	//Saves possible moves
    //An agent selects a move at each state by examining its alternatives via a state evaluation function.
	private static final int maxdepth = 8;	
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
		
	public static double maxevaluation(gamestate pacmannode)
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
	System.out.println(ghostdistance);
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

	public static double minevaluation(gamestate pacmannode)
	{
	double ghostdistance = getNearestGhostDistance(pacmannode.game);// Min value is best
	return ghostdistance;
	}

	private static gamestate Adversarial(final gamestate pacmannode,int depth,boolean maxmin, GHOST g, gamestate best) 
	{
		/*
		 * pacmanmnode: Current pacman game state
		 * depth: Maximum search depth available.
		 * maxmin: show current player(maximizer or minimizer)
		 * best: Best state to go
		 */			
		if (maxmin)
		{
		//maxmin=false;	
		best=maxValue(pacmannode,depth,best); 
        //System.out.println("A"+best.moves);
        Adversarial(pacmannode,depth,false,g,best);
        //System.out.println(depth);
		//return max for pacman
		}
		else
		{ 
		//maxmin=true;	
        //System.out.println("min");
	    best= minValue(pacmannode,depth,g,best);
        //System.out.println("B"+best.moves);
		}
		//maxmin=!maxmin;
		return best;
		}

	public MOVE getMove(Game game, long timeDue) {
	    //Getting next move according to "current game state " then return the next move.
		gamestate pacmannode = new gamestate(game, new LinkedList<MOVE>(),0);
		GHOST[] ghosts = {GHOST.SUE,GHOST.BLINKY,GHOST.INKY,GHOST.PINKY};
		for(GHOST g:ghosts)
		{
			gamestate solution = Adversarial(pacmannode, maxdepth, true, g, pacmannode);
            //System.out.println(solution.moves);   
			possiblemove = new LinkedList<MOVE>(solution.moves);
		}		
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
          
	private static gamestate maxValue(gamestate pacmannode,int depth,gamestate best)
	{
	double a = Integer.MIN_VALUE;
	double most;
	Stack<gamestate> next = pacmansuccessor(pacmannode);
	for(gamestate pacmans : next)
		{
		most = maxevaluation(pacmans);//Best value
		//System.out.println(most);
			if(most >= a)
			{
			a = most;
			best=pacmans;//Best State 
			//System.out.println("a"+best.moves);
			} 
		}
	return best;
	}
	
	private static gamestate minValue(gamestate pacmannode,int depth,GHOST g, gamestate best)
	{
	double b = Integer.MAX_VALUE;
	double least;
	Stack<gamestate> next = ghostsuccessor(pacmannode,g);
	for(gamestate ghosts : next)
		{		
		least = minevaluation(ghosts); 
			if(least <= b)
			{
			b = least;// Lowest value is best
			//System.out.println(least);
			best=ghosts;
			}
		}
	return best;
	}

	private static Stack<gamestate> pacmansuccessor(gamestate pacmannew)
		{
		    //Using last-in-first-out (LIFO) stack for latest gamestate first		
		    Stack<gamestate> pacmansuccessors = new Stack<gamestate>();	
			MOVE[] totalaction;
			totalaction = pacmannew.game.getPossibleMoves(pacmannew.game.getPacmanCurrentNodeIndex(), pacmannew.game.getPacmanLastMoveMade()); 	
			//Traverse all of moves from totalaction
			for(int i=0;i<totalaction.length;i++)
				{
				gamestate successor = null;
				Game middle = pacmannew.game.copy();//Middle State copy this gamestate
				Queue<MOVE> pacmanmoves = new LinkedList<MOVE>(pacmannew.moves);
				//System.out.println(pacmanmoves);
				middle.updatePacMan(totalaction[i]);//Update accroding to Move
				middle.updateGame();
				//Put moves of the gamestate into a Queue
				pacmanmoves.add(totalaction[i]);
				//Create a new game state
				successor = new gamestate(middle,pacmanmoves,pacmannew.depth+3);
				pacmansuccessors.push(successor);
				}	
			//System.out.println(pacmansuccessors);
			return pacmansuccessors;
		}
	
    private static Stack<gamestate> ghostsuccessor(gamestate pacmangnode,GHOST g)
	{
    //Using last-in-first-out (LIFO) stack for dealing with latest gamestate first		
    	Stack<gamestate> ghostsuccessors = new Stack<gamestate>();
	gamestate ghostnext = null;
	MOVE[] ghostaction;
	ghostaction = pacmangnode.game.getPossibleMoves(pacmangnode.game.getGhostCurrentNodeIndex(g),pacmangnode.game.getGhostLastMoveMade(g)); 
    //System.out.println(ghostaction);
		for(int i=0;i<ghostaction.length;i++)				
		{
			Queue<MOVE> ghostmoves = new LinkedList<MOVE>(pacmangnode.moves);
			Game middle = pacmangnode.game.copy();
		    //HashMap gmoves = new HashMap();
		    //gmoves.put(g, ghostaction[i]);
			//Implement EnumMap to meet the requirement of updateGhosts
			EnumMap<GHOST, MOVE> gmoves = new EnumMap<GHOST, MOVE>(GHOST.class);
		    gmoves.put(g, ghostaction[i]);//Put object and value in map
			middle.updateGhosts(gmoves);
			middle.updateGame();
			ghostnext = new gamestate(middle,ghostmoves,pacmangnode.depth +3 );
			ghostsuccessors.push(ghostnext);
			}
    //System.out.println(ghostsuccessors.size());   
	return ghostsuccessors;
	}
	
    
}



