package node;

import java.util.ArrayList;
import java.util.List;

import search.IAction;
import search.IState;

public abstract class Node {
	protected Node father;
	protected List<Node> sons;

	protected int depth;

	protected IState state;


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
		this.father=father;
		this.depth=depth;
		this.state=state;
	}

	public Node getFather() {
		return father;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	public abstract List<Node> getSons();


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
	
	public Node getRightSon(IAction action) {
		for(Node n: getSons()) {
			if(n.getGeneratingMove().equals(action)) { 
				return n;
			}
		}
		return null;
	}
}
