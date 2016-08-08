package entrants.pacman.chengjiezhu;
import java.util.ArrayList;
import java.util.List;
public class State {
	int point;
	List<State> neighborhood;
	boolean visited = false;// Mark all the nodes as not visited
	public State(int point)
	{
	this.point = point;
	this.neighborhood = new ArrayList<State>();
	}
}

