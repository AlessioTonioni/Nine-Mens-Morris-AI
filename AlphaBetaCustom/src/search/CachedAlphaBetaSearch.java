package search;

import java.util.HashMap;
import java.util.Map;

import node.Node;

public class CachedAlphaBetaSearch {
	private int maxDepth;
	private Map<IState, Double>[] alreadyExpanded;
	private int hit=0;
	private int expanded=0;

	public IAction getNextMove(Node root, int maxDepth) {
		this.maxDepth=maxDepth;
		alreadyExpanded=new HashMap[maxDepth+1];
		
		for(int i=1; i<=maxDepth; i++) {
			alreadyExpanded[i] = new HashMap<IState, Double>();
		}
		
		IAction result = null;
		hit=0;
		expanded=0;

		double resultValue = Double.NEGATIVE_INFINITY;
		root.getState().restoreState();
		root.getState().setPhase();
		
		for (Node son : root.getSons()) {
			son.getState().applyAction();
			double value = minValue(son,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
			if (value > resultValue) {
				result = son.getGeneratingMove();
				resultValue = value;
			}
			root.getState().restoreState();
		}
		System.out.println("Nodi espansi: "+ expanded);
		System.out.println("Hit in cache: "+hit);
		return result;
	}

	public double maxValue(Node currentNode, double alpha, double beta, int depth) {
		expanded++;
		if (currentNode.getState().isTerminal() ){
			double value=currentNode.getState().getFinalValue();
			alreadyExpanded[depth].put(currentNode.getState(), value);
			return value+depth;
		}
		if(alreadyExpanded[depth].containsKey(currentNode.getState())){
			Double value=alreadyExpanded[depth].get(currentNode.getState());
			if(value>=beta || depth==maxDepth){
				hit++;
				return value;
			}
		}
		if( depth == maxDepth){
			double value=currentNode.getState().getCutValue();
			alreadyExpanded[depth].put(currentNode.getState(), value);
			return value;
		}
		double value = Double.NEGATIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.max(value, minValue( son, alpha, beta, depth+1));
			if (value >= beta){
				//currentNode.getState().setValue(value);
				//Scommentando questa riga va molto pi� veloce, ma rincretinisce, cosa c'� di sbagliato?
				alreadyExpanded[depth].put(currentNode.getState(), value);
				return value;
			}
			currentNode.getState().restoreState();
			alpha = Math.max(alpha, value);
		}
		alreadyExpanded[depth].put(currentNode.getState(), value);
		return value;
	}

	public double minValue(Node currentNode, double alpha, double beta, int depth) {
		expanded++;
		if (currentNode.getState().isTerminal() ){
			double value=currentNode.getState().getFinalValue();
			alreadyExpanded[depth].put(currentNode.getState(), value);
			return value-depth;
		}
		if(alreadyExpanded[depth].containsKey(currentNode.getState())){
			Double value=alreadyExpanded[depth].get(currentNode.getState());
			if(value <= alpha || depth==maxDepth){
				hit++;
				return value;
			}
		}
		if( depth == maxDepth){
			double value=currentNode.getState().getCutValue();
			alreadyExpanded[depth].put(currentNode.getState(), value);
			return value;
		}
		double value = Double.POSITIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.min(value, maxValue( son, alpha, beta, depth+1));
			if (value <= alpha){
				//currentNode.getState().setValue(value);
				//Scommentando questa riga va molto pi� veloce, ma rincretinisce, cosa c'� di sbagliato?
				alreadyExpanded[depth].put(currentNode.getState(), value);
				return value;
			}
			currentNode.getState().restoreState();
			beta = Math.min(beta, value);
		}
		alreadyExpanded[depth].put(currentNode.getState(), value);
		return value;
	}

}