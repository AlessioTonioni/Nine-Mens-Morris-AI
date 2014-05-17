package prova;

import java.util.ArrayList;
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
		return board.getCutValue(turn);
	}
	

	@Override
	public void setValue(double value) {
		//TODO
	}

	@Override
	public boolean isTerminal() {
		return board.isTerminal(turn,piecesToPlace);
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
	
	@Override
	public void applyAction() {
		board.applyAction(action, turn);
	}


}
