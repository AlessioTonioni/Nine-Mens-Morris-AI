package node;

import java.util.ArrayList;
import java.util.List;

import search.IState;

public class SimpleNode extends Node {

	public SimpleNode(IState initialState) {
		super(initialState);
		// TODO Auto-generated constructor stub
	}
	
	public SimpleNode(IState intialState, int depth){
		super(intialState,null,depth);
	}

	@Override
	public List<Node> getSons() {
		List<Node> sons=new ArrayList<Node>();
		List<IState> newStates=state.getAvailableMoves();
		for(IState s:newStates){
			Node son=new SimpleNode(s,depth+1);
			sons.add(son);
		}
		return sons;
	}

}
