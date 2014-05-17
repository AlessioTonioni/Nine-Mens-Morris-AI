package mills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import search.IAction;
import search.IState;

public class MillsState implements IState{
	
	private Map<Integer, Box> free;
	private Map<Integer, Box> white;
	private Map<Integer, Box> black;
	
	private IAction generatingMove;
	
	private int piecesToPlace;
	private boolean turno;
	
	public MillsState(boolean turno) {
		this.turno = turno;
		this.piecesToPlace = 18;
		//TODO creazione dello stato iniziale
	}
	
	
	public MillsState(Map<Integer, Box> free, Map<Integer, Box> white,
			Map<Integer, Box> black, IAction generatingMove, int piecesToPlace,
			boolean turno) {
		super();
		this.free = free;
		this.white = white;
		this.black = black;
		this.generatingMove = generatingMove;
		this.piecesToPlace = piecesToPlace;
		this.turno = turno;
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
	
	private boolean isPhaseOne(){
		return piecesToPlace > 0;
	}

	private boolean isPhaseTwo(){
		return (turno && white.size()>3) || (!turno && black.size()>3);
	}
	
	private boolean isPhaseThree(){
		return (turno && white.size() == 3) || (!turno && black.size() == 3);
	}
	@Override
	public List<IState> getAvailableMoves() {
		if(isPhaseOne() ) {
			return phaseOne();
		} else if(isPhaseThree()) {
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
					IState newState = this.generatePartialState(tempMove);
					if(checkGeneratingMovesTris(newState)) {
						
					}
				}
			}
		}
		return null; //TODO
	}

	private List<IState> phaseThree() {
		// TODO Auto-generated method stub
		return null;
	}

	private IState generatePartialState(IAction current) {
		MillsState result;
		MillsAction c=(MillsAction) current;
		int indexTo=getIndex(c.getRingTo(), c.getPosTo());
		int indexFrom=-1;
		Box boxTo=free.get(indexTo);
		Box boxFrom=null;
		if(!isPhaseOne()){
			indexFrom=getIndex(c.getRingFrom(), c.getPosFrom());
			boxFrom=(turno)?white.get(indexFrom):black.get(indexFrom);
		}
		
		Box newBoxFrom = from.clone();
		/*Map<Integer,Box> newFree=new HashMap<Integer,Box>();
		for(Integer k:free.keySet()){
			if(k!=indexTo)
				newFree.put(k,free.get(k));
		}
		if(!isPhaseOne())
			newFree.put(indexFrom, boxFrom.clone());
		
		Map<Integer,Box> newWhite=null;
		Map<Integer,Box> newBlack=null;
		
		if(turno){
			newWhite=new HashMap<Integer,Box>();
			for(Integer k:white.keySet()){
				if(k!=indexFrom)
					newWhite.put(k,free.get(k));
			}
			newWhite.put(indexTo, boxTo);
			newBlack=black;
		}		
		else{
			newBlack=new HashMap<Integer,Box>();
			for(Integer k:black.keySet()){
				if(k!=indexFrom)
					newBlack.put(k,free.get(k));
			}
			newBlack.put(indexTo, boxTo);
			newWhite=white;
		}
			*/
		result=new MillsState(newFree, newWhite, newBlack, current, piecesToPlace-1, !turno);
		result.propagateMovement(boxFrom,boxTo);
		return result;
	}

	private void propagateMovement(Box boxFrom, Box boxTo) {
		
	}


	@Override
	public IAction getGeneratingMove() {
		return this.generatingMove;
	}
	
	private boolean checkGeneratingMovesTris(IState stato) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private  int getIndex(int ring, int pos){
		return ring*10+pos;
	}
	private void connect(){
		
	}


	@Override
	public String toString() {
		return "MillsState [free=" + free + ", white=" + white + ", black="
				+ black + ", generatingMove=" + generatingMove
				+ ", piecesToPlace=" + piecesToPlace + ", turno=" + turno + "]";
	}
	
	public static void main(String[] args){
		MillsState
	}

}
