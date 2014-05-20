package search;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private Node father;
	private List<Node> sons;

	private int depth;

	private IState state;


	/**
	 * Costruttore per il nodo root 
	 * @param stato
	 */
	public Node(IState initialState){
		sons=new ArrayList<Node>();
		father=null;
		depth=0;
		state=initialState;
	}

	/**
	 * Costruttore da usare nella fase di espansione dell'albero
	 * @param state
	 * @param father
	 * @param depth
	 */
	public Node(IState state, Node father, int depth ){
		sons=new ArrayList<Node>();
		//this.father=father;
		this.depth=depth;
		this.state=state;
	}

	public Node getFather() {
		return father;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	public List<Node> getSons() {
		/*if(sons.size()==0){
			List<IState> newStates=state.getAvailableMoves();
			for(IState s:newStates){
				Node son=new Node(s,this,depth+1);
				sons.add(son);
			}
		}
		return sons;*/
		List<Node> sons=new ArrayList<Node>();
		List<IState> newStates=state.getAvailableMoves();
		for(IState s:newStates){
			Node son=new Node(s,this,depth+1);
			sons.add(son);
		}
		return sons;
	}


	public int getDepth() {
		return depth;
	}

	public IState getState() {
		return state;
	}

	public boolean isMin(){
		return state.isMin();
	}

	public boolean isMax(){
		return state.isMax();
	}

	public IAction getGeneratingMove() {
		return state.getGeneratingMove();
	}
}
