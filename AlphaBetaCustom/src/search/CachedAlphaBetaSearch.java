package search;

import java.util.HashMap;
import java.util.Map;

import node.Node;

public class CachedAlphaBetaSearch {
	private int maxDepth;
	private Map<IState,Double> alreadyExpanded=new HashMap<IState,Double>();
	private int hit=0;
	private int expanded=0;
	
	public IAction getNextMove(Node root, int maxDepth) {
		this.maxDepth=maxDepth;
		alreadyExpanded.clear();
		IAction result = null;
		hit=0;
		expanded=0;
		
		double resultValue = Double.NEGATIVE_INFINITY;
		root.getState().restoreState();
		
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
			alreadyExpanded.put(currentNode.getState(), value);
			return value+depth;
		}
		if(alreadyExpanded.containsKey(currentNode.getState())){
			hit++;
			return alreadyExpanded.get(currentNode.getState());
		}
		if( depth == maxDepth){
			double value=currentNode.getState().getCutValue();
			alreadyExpanded.put(currentNode.getState(), value);
			return value;
		}
		double value = Double.NEGATIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.max(value, minValue( son, alpha, beta, depth+1));
			if (value >= beta){
				currentNode.getState().setValue(value);
				//Scommentando questa riga va molto più veloce, ma rincretinisce, cosa c'è di sbagliato?
				//alreadyExpanded.put(currentNode.getState(), value);  
				return value;
			}
			currentNode.getState().restoreState();
			alpha = Math.max(alpha, value);
		}
		alreadyExpanded.put(currentNode.getState(), value);
		return value;
	}

	public double minValue(Node currentNode, double alpha, double beta, int depth) {
		expanded++;
		if (currentNode.getState().isTerminal() ){
			double value=currentNode.getState().getFinalValue();
			alreadyExpanded.put(currentNode.getState(), value);
			return value-depth;
		}
		if(alreadyExpanded.containsKey(currentNode.getState())){
			hit++;
			return alreadyExpanded.get(currentNode.getState());
		}
		if( depth == maxDepth){
			double value=currentNode.getState().getCutValue();
			alreadyExpanded.put(currentNode.getState(), value);
			return value;
		}
		double value = Double.POSITIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.min(value, maxValue( son, alpha, beta, depth+1));
			if (value <= alpha){
				currentNode.getState().setValue(value);
				//Scommentando questa riga va molto più veloce, ma rincretinisce, cosa c'è di sbagliato?
				//alreadyExpanded.put(currentNode.getState(), value);
				return value;
			}
			currentNode.getState().restoreState();
			beta = Math.min(beta, value);
		}
		alreadyExpanded.put(currentNode.getState(), value);
		return value;
	}
	
}
