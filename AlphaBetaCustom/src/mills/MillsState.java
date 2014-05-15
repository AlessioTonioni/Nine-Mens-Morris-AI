package mills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import search.IAction;
import search.IState;

public class MillsState implements IState {
	
	private Map<Integer, Box> free;
	private Map<Integer, Box> white;
	private Map<Integer, Box> black;
	
	private IAction generatingMove;
	
	private int piecesToPlace;
	private boolean turno;
	
	public MillsState(boolean turno) {
		this.turno = turno;
		this.piecesToPlace = 18;
	}

	@Override
	public boolean isMin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMax() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setValue(double value) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isTerminal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IState> getAvailableMoves() {
		if(piecesToPlace > 0 ) {
			return phaseOne();
		} else if((turno && white.size() == 3) || (!turno && black.size() == 3)) {
			return phaseThree();
		} else {
			return phaseTwo();
		}
	}
	
	private List<IState> phaseOne() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<IState> phaseTwo() {
		List<IAction> result = new ArrayList<IAction>();
		if(turno) {
			for(Box currentBox: white.values()) {
				List<IAction> temp = currentBox.getPossibleMovements();
				for(IAction tempMove : temp) {
					IState newState = this.apply(tempMove);
					if(checkGeneratingMovesTris()) {
						
					}
				}
			}
		}
	}

	private List<IState> phaseThree() {
		// TODO Auto-generated method stub
		return null;
	}

	private IState apply(IAction current) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAction getGeneratingMove() {
		return this.generatingMove;
	}
	
	private boolean checkGeneratingMovesTris() {
		// TODO Auto-generated method stub
		return false;
	}

}
