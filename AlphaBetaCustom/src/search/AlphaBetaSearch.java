package search;

import node.Node;

public class AlphaBetaSearch {
	private int maxDepth;
	private int expanded=0;

	public IAction getNextMove(Node root, int maxDepth) {
		this.maxDepth=maxDepth;
		IAction result = null;
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
		return result;
	}

	public double maxValue(Node currentNode, double alpha, double beta, int depth) {
		expanded++;
		if (currentNode.getState().isTerminal() ){
			return currentNode.getState().getFinalValue()+depth;
		}
		if( depth == maxDepth){
			return currentNode.getState().getCutValue();
		}
		double value = Double.NEGATIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.max(value, minValue( son, alpha, beta, depth+1));
			if (value >= beta){
				//currentNode.getState().setValue(value);
				return value;
			}
			currentNode.getState().restoreState();
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(Node currentNode, double alpha, double beta, int depth) {
		expanded++;
		if (currentNode.getState().isTerminal() ){
			return currentNode.getState().getFinalValue()-depth;
		}
		if( depth == maxDepth){
			return currentNode.getState().getCutValue();
		}
		double value = Double.POSITIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.min(value, maxValue( son, alpha, beta, depth+1));
			if (value <= alpha){
				//currentNode.getState().setValue(value);
				return value;
			}
			currentNode.getState().restoreState();
			beta = Math.min(beta, value);
		}
		return value;
	}

}
