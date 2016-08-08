package entrants.pacman.chengjiezhu;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;


public class DFS extends PacmanController{
	static int index=0;
	public static MOVE DFS(Game game)
	{
	int current = game.getPacmanCurrentNodeIndex();
	int[] pills = game.getActivePillsIndices();
	int[] powers = game.getActivePowerPillsIndices(); 
	Stack<Integer> target = new Stack<Integer>();
	//put all pills and powers into target
	    for(int pill:pills)
			{
			target.add(pill);
			}
	    for(int power:powers)
			{
			target.add(power);
			}
	//startpoint is current index    
	State startpoint = new State(current);
    //System.out.println(target.size());
	for(int index=0;index<target.size();index++)
		{
		if(target.size()>0)
			{
		    //target.size=the number of visible pills and powers
			//Create State list whose size is the number of visible pils and powers
			State[] nexts = new State[target.size()];
			//Put each index of target to nexts
			for(int i=0;i<target.size();i++)
				{
			     nexts[i] = new State(target.get(i));
				}
			
			for(int i=0;i<target.size()-1;i++)
				{
			nexts[i+1].neighborhood.add(nexts[i]);
			//Be connected to a line,i.e.:list.size=10 -> 9 pairs
				}
			startpoint.neighborhood.add(nexts[0]);
			}			
		else 
			{
			     Random random = new Random();
				 MOVE[] randommove = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
				 System.out.println(randommove);
				 return randommove[random.nextInt(randommove.length)];	
			}
		}	
	
	Stack<State> stack = new Stack<State>();//The differenece between DFS and BFS
	stack.push(startpoint);
	for(int i=0;i<startpoint.neighborhood.size();i++)
	//Recur for all the nodes adjacent to this node
	{	
	State path = startpoint.neighborhood.get(i);
	if(!path.visited)//if there is still unvisited node
		{
		path.visited = true;	
		stack.push(path);//Push to stack if unvisited
		}
	}
	MOVE move = game.getNextMoveTowardsTarget(current,stack.pop().point, DM.PATH);
	return move;
	}
	public MOVE getMove(Game game, long timeDue) {
	//Add following part can increase a  little bit survive time of pacman by letting pacman eat a nearest power pill at first time
		
		int order = game.getPacmanCurrentNodeIndex();
	    //System.out.println(order+",");		
		if(index++<50)
			{
			//System.out.println(index+",");
				return MOVE.LEFT;
			}	
			else if (index++<100)
			{
				return MOVE.DOWN;	
			}
			else if (index++<130)
			{
				return MOVE.RIGHT;	
			}
			else if (index++<180)
			{
		        return MOVE.UP;	
			}
			else{
				
				if(order<1122&&order>1110)
				{
					return MOVE.LEFT;	
				}
				if(order==1110)
			   {
				int count=3;					
				if(count%2==1)
				{
					count++;
					return MOVE.UP;}			
				else
				{
					count++;return MOVE.RIGHT;
				}
			    }
				MOVE move = DFS(game);
				return move;
				}
	}

}








