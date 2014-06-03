package search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import node.Node;

public class CachedAlphaBetaSearchAux {
	private int maxDepth;
	private Map<IState,StateValue>[] alreadyExpanded;
	
	public IAction getNextMove(Node root, int maxDepth) {
		this.maxDepth=maxDepth;
		alreadyExpanded=new HashMap[maxDepth+1];
			
		for(int i=1; i<=maxDepth; i++) {
			alreadyExpanded[i] = new HashMap<IState, StateValue>();
		}
		IAction result = null;
		
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
		//System.out.println("Nodi espansi: "+ expanded);
		//System.out.println("Hit in cache: "+hit);
		//System.out.println("Nodi saltati: "+skipped);
		//System.out.println("Nodi ripresi: "+continued);
		
		System.out.println(result);
		
		if(result == null) {
			System.out.println("Nessuna mossa disponibile!!!");
		}
		
		return result;
	}

	public double maxValue(Node currentNode, double alpha, double beta, int depth) {
		double value = Double.NEGATIVE_INFINITY;
		int last = 0;
		if(alreadyExpanded[depth].containsKey(currentNode.getState())){
			StateValue stateValue =  alreadyExpanded[depth].get(currentNode.getState());
			if(stateValue.isTerminal())
				return stateValue.getValue() + depth;
			else if(stateValue.isValid())
				return stateValue.getValue();
			value = stateValue.getValue();
			if (value >= beta)
			{
				return value;
			}
			last = stateValue.getLastExpanded() + 1;
		}
		else {
			if (currentNode.getState().isTerminal() ){
				value=currentNode.getState().getFinalValue();
				alreadyExpanded[depth].put(currentNode.getState(), new StateValue(true, true, value, 0));
				return value+depth;
			}
			else if( depth == maxDepth){
				value=currentNode.getState().getCutValue();
				alreadyExpanded[depth].put(currentNode.getState(), new StateValue(false, true, value, 0));
				return value;
			}
		}
		List<Node> sonsList = currentNode.getSons();
		for (int i = last; i<sonsList.size();i++) {
			Node son = sonsList.get(i);
			son.getState().applyAction();
			value = Math.max(value, minValue( son, alpha, beta, depth+1));
			if (value >= beta){
				//currentNode.getState().setValue(value);
				alreadyExpanded[depth].put(currentNode.getState(), new StateValue(false, false, value, i));  
				return value;
			}
			currentNode.getState().restoreState();
			alpha = Math.max(alpha, value);
		}
		alreadyExpanded[depth].put(currentNode.getState(), new StateValue(false, true, value, 0));
		return value;
	}

	public double minValue(Node currentNode, double alpha, double beta, int depth) {
		double value = Double.POSITIVE_INFINITY;
		int last = 0;
		if(alreadyExpanded[depth].containsKey(currentNode.getState())){
			StateValue stateValue =  alreadyExpanded[depth].get(currentNode.getState());
			if(stateValue.isTerminal())
				return stateValue.getValue() - depth;
			else if(stateValue.isValid())
				return stateValue.getValue();
			value = stateValue.getValue();
			if (value <= alpha)
			{
				return value;
			}
			last = stateValue.getLastExpanded() + 1;
		}
		else {
			if (currentNode.getState().isTerminal() ){
				value=currentNode.getState().getFinalValue();
				alreadyExpanded[depth].put(currentNode.getState(), new StateValue(true, true, value, 0));
				return value - depth;
			}
			else if( depth == maxDepth){
				value=currentNode.getState().getCutValue();
				alreadyExpanded[depth].put(currentNode.getState(), new StateValue(false, true, value, 0));
				return value;
			}
		}
		List<Node> sonsList = currentNode.getSons();
		for (int i = last; i<sonsList.size();i++) {
			Node son = sonsList.get(i);
			son.getState().applyAction();
			value = Math.min(value, maxValue( son, alpha, beta, depth+1));
			if (value <= alpha){
				//currentNode.getState().setValue(value);
				alreadyExpanded[depth].put(currentNode.getState(), new StateValue(false, false, value, i));  
				return value;
			}
			currentNode.getState().restoreState();
			beta = Math.min(beta, value);
		}
		alreadyExpanded[depth].put(currentNode.getState(), new StateValue(false, true, value, 0));
		return value;
	}
	
}
