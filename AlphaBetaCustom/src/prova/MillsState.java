package prova;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import prova.MillsBoard.color;
import mills.MillsAction;
import search.IAction;
import search.IState;

public class MillsState implements IState {
	
	private boolean turn;
	private int piecesToPlace;
	private MillsBoard board;
	
	private color[] currentState;
	private MillsAction action;
	
	public MillsState(boolean turn, int piecesToPlace) {
		this.turn = turn;
		this.piecesToPlace = piecesToPlace;
		this.board = MillsBoard.getInstance();
		this.currentState = new color[24];
		for(int i=0;i<24;i++) {
			currentState[i] = color.empty;
		}
	}
	
	public MillsState(boolean turn, int piecesToPlace, color[] currentstate, MillsAction action){
		this(turn,piecesToPlace);
		this.currentState=currentstate;
		this.action=action;
	}

	@Override
	public boolean isMin() {
		return !turn;
	}

	@Override
	public boolean isMax() {
		return turn;
	}

	@Override
	public double getFinalValue() {
		return board.getFinalValue(turn);
	}
	
	@Override
	public double getCutValue() {
		return board.getCutValue(piecesToPlace);
	}

	@Override
	public void setValue(double value) {
		//TODO ????
	}

	@Override
	public boolean isTerminal() {
		return board.isTerminal(turn,piecesToPlace);
	}

	@Override
	public int getPiecesToPlace(){
		return piecesToPlace;
	}
	
	@Override
	public List<IState> getAvailableMoves() {
		List<IState> result=new ArrayList<IState>();
	
		color[] tempState=board.serialize();
		
		List<MillsAction> actions = board.getAvailableMoves(turn, piecesToPlace,false);
		for(MillsAction a:actions){
			result.add(new MillsState(!turn, piecesToPlace-1, tempState, a));
		}
		
		return result;
	}

	@Override
	public IAction getGeneratingMove() {
		return action;
	}

	@Override
	public void restoreState(){
		board.deSerialize(currentState);
	}
	
	public void setState(color[] state) {
		currentState = state;
		restoreState();
	}
	
	@Override
	public void applyAction() {
		board.applyAction(action, !turn);
		currentState=board.serialize();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(currentState);
		result = prime * result + piecesToPlace;
		result = prime * result + (turn ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MillsState other = (MillsState) obj;
		if (!Arrays.equals(currentState, other.currentState))
			return false;
		if (piecesToPlace != other.piecesToPlace)
			return false;
		if (turn != other.turn)
			return false;
		return true;
	}




}
