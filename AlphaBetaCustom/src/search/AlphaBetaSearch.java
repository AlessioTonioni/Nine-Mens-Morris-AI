package search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import mills.MillsAction;
import prova.MillsBoard;
import prova.MillsState;

public class AlphaBetaSearch {
	private int maxDepth;

	public IAction getNextMove(Node root, int maxDepth) {
		this.maxDepth=maxDepth;
		IAction result = null;
		
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
		return result;
	}

	public double maxValue(Node currentNode, double alpha, double beta, int depth) {
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
				currentNode.getState().setValue(value);
				return value;
			}
			currentNode.getState().restoreState();
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(Node currentNode, double alpha, double beta, int depth) {
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
		Node root = new Node(state);
		AlphaBetaSearch search = new AlphaBetaSearch();
		IAction action = search.getNextMove(root, 2);
		System.out.println("Action: "+action);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		Node newNode = root;
		
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
				
				newNode.getState().restoreState();
				MillsBoard.getInstance().applyAction((MillsAction)action, true);
				MillsBoard.getInstance().applyAction(posring,false);
				
				MillsState s=new MillsState(true, newNode.getState().getPiecesToPlace()-2);
				s.setState(MillsBoard.getInstance().serialize());
				newNode=new Node(s);
				
				//newNode = newNode.getRightSon(action).getRightSon(posring);
				//newNode.setFather(null);
				action=search.getNextMove(newNode, 7);
				System.out.println(action);
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		} while(!newNode.getState().isTerminal());
		System.out.println("Nodo terminale.");
	}
}
