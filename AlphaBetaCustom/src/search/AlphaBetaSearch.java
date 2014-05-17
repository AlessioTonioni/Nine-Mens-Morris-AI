package search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import prova.MillsBoard;
import prova.MillsState;

public class AlphaBetaSearch {
	private int maxDepth;

	public IAction getNextMove(Node root, int maxDepth) {
		this.maxDepth=maxDepth;
		IAction result = null;
		
		double resultValue = Double.NEGATIVE_INFINITY;
		for (Node son : root.getSons()) {
			son.getState().applyAction();
			double value = minValue(son,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (value > resultValue) {
				result = son.getGeneratingMove();
				resultValue = value;
			}
			root.getState().restoreState();
		}
		return result;
	}

	public double maxValue(Node currentNode, double alpha, double beta) {
		if (currentNode.getState().isTerminal() ){
			return currentNode.getState().getFinalValue();
		}
		if( currentNode.getDepth() == maxDepth){
			return currentNode.getState().getCutValue();
		}
		double value = Double.NEGATIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.max(value, minValue( son, alpha, beta));
			if (value >= beta){
				currentNode.getState().setValue(value);
				return value;
			}
			currentNode.getState().restoreState();
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(Node currentNode, double alpha, double beta) {
		if (currentNode.getState().isTerminal() ){
			return currentNode.getState().getFinalValue();
		}
		if( currentNode.getDepth() == maxDepth){
			return currentNode.getState().getCutValue();
		}
		double value = Double.POSITIVE_INFINITY;
		for (Node son : currentNode.getSons()) {
			son.getState().applyAction();
			value = Math.min(value, maxValue( son, alpha, beta));
			if (value <= alpha){
				currentNode.getState().setValue(value);
				return value;
			}
			currentNode.getState().restoreState();
			beta = Math.min(beta, value);
		}
		return value;
	}
	
	public static void main(String[] args) {
		MillsState state = new MillsState(true, 18);
		Node n = new Node(state);
		AlphaBetaSearch search = new AlphaBetaSearch();
		
		IAction action = search.getNextMove(n, 2);
		System.out.println("Action: "+action);
		/*
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		MillsState previousState = null;
		do{
			System.out.println("Inserisci prossima mossa: ");
			try {
				String line = in.readLine();
				StringTokenizer st = new StringTokenizer(line);
				int ringFrom = Integer.parseInt(st.nextToken());
				int posFrom = Integer.parseInt(st.nextToken());
				int ringTo = Integer.parseInt(st.nextToken());
				int posTo = Integer.parseInt(st.nextToken());
				int ringDelete = Integer.parseInt(st.nextToken());
				int posDelete = Integer.parseInt(st.nextToken());
				MillsAction posring = new MillsAction(ringFrom, posFrom, ringTo, posTo, ringDelete, posDelete);
				MillsState newState = previousState.performAction(posring);
				
				action=search.makeDecision(newState);
				System.out.println(action);
				previousState=gioco.getResult(newState, action);
				previousState.reset(8);
				
			} catch (Exception e) {
				System.out.println("Non ho capito.");
				continue;
			}
		} while(!previousState.isTerminal());*/
	}
}
