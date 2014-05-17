package prova;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mills.Box;
import mills.MillsAction;

public class MillsBoard {

	private static MillsBoard instance = null;

	private Map<Integer,Box> boxes; 

	private Map<Integer, Box> free;
	private Map<Integer, Box> white;
	private Map<Integer, Box> black;

	private MillsBoard() {
		free = new HashMap<Integer, Box>();
		white = new HashMap<Integer, Box>();
		black = new HashMap<Integer, Box>();
		boxes = new HashMap<Integer, Box>();

		for(int i=0; i<3; i++) {
			for(int j=0; j<8;j++) {
				Box temp=Box.generateBox(i, j);
				free.put(getIndex(i, j), temp);
				boxes.put(getIndex(i, j), temp);
			}
		}
		connect();
	}

	public static MillsBoard getInstance() {
		if(instance == null) {
			instance = new MillsBoard();
		}

		return instance;
	}

	public static int getIndex(int ring, int pos){
		int ciccio = ring*10+pos;
		return ciccio;
	}

	public List<MillsAction> getAvailableMoves(boolean turn, int piecesToPlace, boolean noDelete) {
		if(piecesToPlace > 0) {
			return phaseOne(turn);
		} else if((turn && white.size() <=3) || (!turn && black.size() <=3)) {
			return phaseThree(turn);
		} else {
			return phaseTwo(turn, noDelete);
		}
	}

	private List<MillsAction> phaseOne(boolean turn) {
		List<MillsAction> result = new ArrayList<MillsAction>();
		for(Box b: free.values()) {
			MillsAction action = new MillsAction(-1, -1, b.getRing(), b.getPos(), -1, -1);
			applyAndCheck(action, turn, result);
		}
		return result;
	}

	private Box move(MillsAction action, Map<Integer, Box> listFrom, Map<Integer, Box> listTo, boolean turn) {
		if(action.getRingFrom() != -1) {
			int from = getIndex(action.getRingFrom(), action.getPosFrom());
			Box newBox = listFrom.get(from);
			newBox.setBlack(false);
			newBox.setWhite(false);
		}
		if(action.getRingTo() != -1) {
			int to = getIndex(action.getRingTo(), action.getPosTo());
			Box bFree = listTo.get(to);
			bFree.setBlack(!turn);
			bFree.setWhite(turn);
			return bFree;
		}
		return null;
	}

	private void restoreFromAction(MillsAction action, boolean turn) {
		MillsAction reverseAction = action.reverse();
		if(turn) {
			move(reverseAction, free, white, turn);
		} else {
			move(reverseAction, free, black, turn);
		}
	}

	private void applyAndCheck(MillsAction action, boolean turn, List<MillsAction> totalActions) {
		Box newBox = null;
		if(turn) {
			newBox = move(action, white, free, turn);
		} else {
			newBox = move(action, black, free, turn);
		}
		if(newBox.isOnTris()) {
			List<MillsAction> millsAction = generateDelete(action, turn);
			totalActions.addAll(millsAction);
		} else {
			totalActions.add(action);
		}
		restoreFromAction(action, turn);
		return;
	}

	private MillsAction generate(Box b, MillsAction action) {
		MillsAction newAction = action.clone();
		if(!b.isOnTris()) {
			newAction.setRingDelete(b.getRing());
			newAction.setPosDelete(b.getPos());
		}
		return newAction;
	}

	private List<MillsAction> generateDelete(MillsAction action, boolean turn) {
		List<MillsAction> result = new ArrayList<MillsAction>();
		if(turn) {
			for(Box b: black.values()) {
				result.add(generate(b, action));
			}
		} else {
			for(Box b: white.values()) {
				result.add(generate(b, action));
			}
		}
		return result;
	}

	private List<MillsAction> generatePhaseTwo(boolean turn, Map<Integer, Box> choice, boolean noDelete) {
		List<MillsAction> result = new ArrayList<MillsAction>();
		for(Box currentBox: choice.values()) {
			List<MillsAction> temp = currentBox.getPossibleMovements();
			if(!noDelete){
				for(MillsAction action: temp) {
					applyAndCheck(action, turn, result);
				}
			}
		}
		return result;
	}

	private List<MillsAction> phaseTwo(boolean turn, boolean noDelete) {
		if(turn) {
			return generatePhaseTwo(turn, white, noDelete);
		} else {
			return generatePhaseTwo(turn, black, noDelete);
		}
	}

	private List<MillsAction> generatePhaseThree(boolean turn, Map<Integer, Box> choice) {
		List<MillsAction> result = new ArrayList<MillsAction>();
		for(Box start: choice.values()) {
			for(Box b: free.values()) {
				MillsAction action = new MillsAction(start.getRing(), start.getPos(), b.getRing(), b.getPos(), -1, -1);
				applyAndCheck(action, turn, result);
			}
		}
		return result;
	}

	private List<MillsAction> phaseThree(boolean turn) {
		if(turn) {
			return generatePhaseThree(turn, white);
		} else {
			return generatePhaseThree(turn, black);
		}
	}

	private void connect(){
		for(Box b: free.values()) {
			b.connect(free);
		}
	}

	public void applyAction(MillsAction mossa, boolean turn){
		if(mossa==null) return;
		Map<Integer,Box> from=(turn)?white:black;
		Map<Integer, Box> to=free;

		int ciccio = getIndex(mossa.getRingTo(), mossa.getPosTo());
		Box destination=to.get(ciccio);
		destination.setWhite(turn);
		destination.setBlack(!turn);
		to.remove(getIndex(mossa.getRingTo(), mossa.getPosTo()));
		from.put(getIndex(mossa.getRingTo(), mossa.getPosTo()),destination);

		if(mossa.getRingFrom()!=-1){
			Box fromBox=from.get(getIndex(mossa.getRingFrom(), mossa.getPosFrom()));
			fromBox.setBlack(false);
			fromBox.setWhite(false);
			from.remove(getIndex(mossa.getRingFrom(), mossa.getPosFrom()));
			free.put(getIndex(mossa.getRingFrom(), mossa.getPosFrom()), fromBox);
		} 
		if(mossa.getRingDelete()!=-1){
			Map<Integer,Box> enemy=(!turn)?white:black;
			Box deletedBox=enemy.get(getIndex(mossa.getRingFrom(), mossa.getPosFrom()));
			deletedBox.setBlack(false);
			deletedBox.setWhite(false);
			enemy.remove(getIndex(mossa.getRingFrom(), mossa.getPosFrom()));
			free.put(getIndex(mossa.getRingFrom(), mossa.getPosFrom()), deletedBox);
		}
	}

	public color[] serialize(){
		color[] result=new color[24];
		for(Box b:free.values()){
			result[indexOf(b)]=color.empty;
		}
		for(Box b:white.values()){
			result[indexOf(b)]=color.white;
		}
		for(Box b:black.values()){
			result[indexOf(b)]=color.black;
		}
		return result;
	}

	public void deSerialize(color[] state){
		free.clear();
		black.clear();
		white.clear();
		for(int i=0; i<state.length; i++){
			int index=getIndex(getSerializedRing(i), getSerializedPos(i));
			switch(state[i]){
			case black:
				black.put(index,boxes.get(index));
				break;
			case empty:
				free.put(index, boxes.get(index));
				break;
			case white:
				white.put(index,boxes.get(index));
				break;
			default:
				break;

			}
		}
	}

	public void deSerialize(color[] state, MillsAction mossa, boolean turn){
		deSerialize(state);
	}
	private int indexOf(Box b){
		return 7*b.getRing()+b.getPos();
	}

	private int getSerializedRing(int i){
		return i/7;
	}

	private int getSerializedPos(int i){
		return i%7;
	}

	public enum color{
		empty,white,black;
	}


	public boolean isTerminal(boolean turn, int piecesToPlace) {
		Map<Integer,Box> mine=(turn)?white:black;
		return mine.size()<3 || 
				getAvailableMoves(turn, piecesToPlace, true).size()==0;
	}

	public double getFinalValue(boolean turn) {
		return (turn)?-1000:1000;		
	}
	
	public double getCutValue(boolean turn){
		return 0;
		//TODO euristica;
	}

}
