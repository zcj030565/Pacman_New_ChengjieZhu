package entrants.pacman.chengjiezhu;

import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;
import pacman.game.Game;
import java.util.*;
import entrants.pacman.chengjiezhu.State;
import static pacman.game.Constants.*;
import pacman.controllers.PacmanController;

public class BFS extends PacmanController{

	static int index=0;
	
	public static MOVE BFS(Game game)
	{
	System.out.println("x");	
	int current = game.getPacmanCurrentNodeIndex();
	int[] pills = game.getActivePillsIndices();
	int[] powers = game.getActivePowerPillsIndices(); 
	Stack<Integer> target = new Stack<Integer>();//put all pills and powers into target
	    for(int pill:pills)
			{
			target.add(pill);
			}
	    for(int power:powers)
			{
			target.add(power);
			}
	    
	State startpoint = new State(current);
	for(int index=0;index<target.size();index++)
		{
		if(target.size()>0)
		{
		//target.size=the number of visible pills and powers
		State[] nexts = new State[target.size()];
		//Create State list whose size is the number of visible pils and powers
		for(int i=0;i<target.size();i++)
			{
		     nexts[i] = new State(target.get(i));
			}
		
		for(int i=0;i<target.size()-1;i++)
			{
		//Start from the nearest index first
		nexts[i+1].neighborhood.add(nexts[i]);
		//Be connected to a line,i.e.:list.size=10 -> 9 pairs
			}
		startpoint.neighborhood.add(nexts[0]);
		//System.out.println(startpoint.neighborhood);
		}
    else 
		{
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
			return MOVE.LEFT;
		}
	    else if(game.getNeighbour(current, MOVE.DOWN)<0)
		{
	    	return MOVE.UP;
	    	}
	    else 
	    {
	     Random random = new Random();
		 MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
		 return moves[random.nextInt(moves.length)];	
	     }
		}
		}
	    System.out.println("y");	
		Queue<State> queue = new LinkedList<State>();//The differenece between DFS and BFS		
		queue.add(startpoint);
		System.out.println(queue);	
		for(int i=startpoint.neighborhood.size()-1;i>1;i--)//Recur for all the nodes adjacent to this node
		{
		System.out.println(i);	
		State path = startpoint.neighborhood.get(i);
		queue.add(path);
		}
		MOVE move = game.getNextMoveTowardsTarget(current,queue.poll().point, DM.PATH);
		return move;
		}
	
	public MOVE getMove(Game game, long timeDue) {
		/*Add following part can increase a  little bit survive time of pacman by letting pacman eat a nearest power pill at first time
		*/
		int order = game.getPacmanCurrentNodeIndex();	
				MOVE move = BFS(game);
				return move;

		}
	}
	
    



