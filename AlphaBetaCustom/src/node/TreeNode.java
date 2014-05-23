package node;

import java.util.List;

import search.IState;

public class TreeNode extends Node {

	public TreeNode(IState initialState) {
		super(initialState);
		// TODO Auto-generated constructor stub
	}

	public TreeNode(IState initialState,Node father, int depth){
		super(initialState,father,depth);	
		}

	@Override
	public List<Node> getSons() {
		if(sons.size()==0){
			List<IState> newStates=state.getAvailableMoves();
			for(IState s:newStates){
				Node son=new TreeNode(s,this,depth+1);
				sons.add(son);
			}
		}
		return sons;
	}

}
