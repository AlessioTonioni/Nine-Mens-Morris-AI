package search;

public class AlphaBetaSearch {
	private int maxDepth;

	public IAction getNextMove(Node root, int maxDepth) {
		this.maxDepth=maxDepth;
		IAction result = null;
		
		double resultValue = Double.NEGATIVE_INFINITY;
		for (Node son : root.getSons()) {
			double value = minValue(son,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (value > resultValue) {
				result = son.getGeneratingMove();
				resultValue = value;
			}
		}
		return result;
	}

	public double maxValue(Node currentNode, double alpha, double beta) {
		if (currentNode.getState().isTerminal() || currentNode.getDepth() == maxDepth){
			return currentNode.getState().getValue();
		}
		double value = Double.NEGATIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			value = Math.max(value, minValue( son, alpha, beta));
			if (value >= beta){
				currentNode.getState().setValue(value);
				return value;
			}
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(Node currentNode, double alpha, double beta) {
		if (currentNode.getState().isTerminal() || currentNode.getDepth() == maxDepth){
			return currentNode.getState().getValue();
		}
		double value = Double.POSITIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			value = Math.min(value, maxValue( son, alpha, beta));
			if (value <= alpha){
				currentNode.getState().setValue(value);
				return value;
			}
			beta = Math.min(beta, value);
		}
		return value;
	}
}
