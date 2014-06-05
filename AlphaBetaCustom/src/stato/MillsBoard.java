package stato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mills.Box;
import mills.MillsAction;

public class MillsBoard {

	private static MillsBoard instance = null;

	public Map<Integer,Box> boxes; 

	public Map<Integer, Box> free;
	public Map<Integer, Box> white;
	public Map<Integer, Box> black;

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
		return ring*10+pos;
	}
	
	public int getWhite() {
		return white.size();
	}
	
	public int getBlack() {
		return black.size();
	}
	
	public int getWhiteAvailableMoves() {
		int sum = 0;
		for(Box b: white.values()) {
			sum+=b.getPossibleMovements().size();
		}
		return sum;
	}
	
	public int getCountTL(boolean turn) {
		int sum = 0;
		for(Box b: free.values()) {
			sum+=b.countTL(turn);
		}
		return sum;
	}
	
	public int getBlackAvailableMoves() {
		int sum = 0;
		for(Box b: black.values()) {
			sum+=b.getPossibleMovements().size();
		}
		return sum;
	}
	
	public int getWhiteNumberOfTris() {
		int sum = 0;
		for(Box b: white.values()) {
			sum+=(b.isOnTris()?1:0);
		}
		return (sum/3)+1;
	}
	
	public int getBlackNumberOfTris() {
		int sum = 0;
		for(Box b: black.values()) {
			sum+=(b.isOnTris()?1:0);
		}
		return (sum/3)+((sum%3==0)?0:1);
	}
	
	public boolean isWhitePhaseThree(int piecesToPlace) {
		return piecesToPlace <= 0 && white.size() <= 3;
	}
	
	public boolean isBlackPhaseThree(int piecesToPlace) {
		return piecesToPlace <= 0 && black.size() <= 3;
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
			applyAndCheck(action, turn, result, false);
		}
		return result;
	}

	private Box move(MillsAction action, Map<Integer, Box> listFrom, Map<Integer, Box> listTo, boolean turn) {
		if(action.getRingFrom() != -1) {
			int from = getIndex(action.getRingFrom(), action.getPosFrom());
			Box newBox = listFrom.get(from);
			newBox.setFree();
		}
		if(action.getRingTo() != -1) {
			int to = getIndex(action.getRingTo(), action.getPosTo());
			Box bFree = listTo.get(to);
			if(turn)
				bFree.setWhite();
			else
				bFree.setBlack();
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

	private void applyAndCheck(MillsAction action, boolean turn, List<MillsAction> totalActions, boolean phaseThree) {
		Box newBox = null;
		if(turn) {
			newBox = move(action, white, free, turn);
		} else {
			newBox = move(action, black, free, turn);
		}
		if(newBox.isOnTris()) {
			List<MillsAction> millsAction = generateDelete(action, turn, phaseThree);
			totalActions.addAll(millsAction);
		} else {
			totalActions.add(action);
		}
		restoreFromAction(action, turn);
		return;
	}

	private List<MillsAction> generateDelete(MillsAction action, boolean turn, boolean phaseThree) {
		List<MillsAction> result = new ArrayList<MillsAction>();
		if(turn) {
			for(Box b: black.values()) {
				if(!b.isOnTris() || phaseThree) {
					MillsAction newAction = action.clone();
					newAction.setRingDelete(b.getRing());
					newAction.setPosDelete(b.getPos());
					result.add(newAction);
				}
			}
		} else {
			for(Box b: white.values()) {
				if(!b.isOnTris() || phaseThree) {
					MillsAction newAction = action.clone();
					newAction.setRingDelete(b.getRing());
					newAction.setPosDelete(b.getPos());
					result.add(newAction);
				}
			}
		}
		if(result.size() == 0) {
			return generateDelete(action, turn, true);
		}
		
		return result;
	}

	private List<MillsAction> generatePhaseTwo(boolean turn, Map<Integer, Box> choice, boolean noDelete) {
		List<MillsAction> result = new ArrayList<MillsAction>();
		for(Box currentBox: choice.values()) {
			List<MillsAction> temp = currentBox.getPossibleMovements();
			if(!noDelete){
				for(MillsAction action: temp) {
					applyAndCheck(action, turn, result, false);
				}
			} else {
				result.addAll(temp);
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
				applyAndCheck(action, turn, result, true);
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

		Box destination=to.get(getIndex(mossa.getRingTo(), mossa.getPosTo()));
		if(turn)
			destination.setWhite();
		else
			destination.setBlack();
		to.remove(getIndex(mossa.getRingTo(), mossa.getPosTo()));
		from.put(getIndex(mossa.getRingTo(), mossa.getPosTo()),destination);

		if(mossa.getRingFrom()!=-1){
			Box fromBox=from.get(getIndex(mossa.getRingFrom(), mossa.getPosFrom()));
			fromBox.setFree();
			from.remove(getIndex(mossa.getRingFrom(), mossa.getPosFrom()));
			free.put(getIndex(mossa.getRingFrom(), mossa.getPosFrom()), fromBox);
		} 
		if(mossa.getRingDelete()!=-1){
			Map<Integer,Box> enemy=(turn)?black:white;
			Box deletedBox=enemy.get(getIndex(mossa.getRingDelete(), mossa.getPosDelete()));
			deletedBox.setFree();
			enemy.remove(getIndex(mossa.getRingDelete(), mossa.getPosDelete()));
			free.put(getIndex(mossa.getRingDelete(), mossa.getPosDelete()), deletedBox);
		}
	}

	public color[] serialize(){
		color[] result=new color[24];
		for(Box b:free.values()){
			if(b!=null)
				result[indexOf(b)]=color.empty;
		}
		for(Box b:white.values()){
			if(b!=null)
				result[indexOf(b)]=color.white;
		}
		for(Box b:black.values()){
			if(b!=null)
				result[indexOf(b)]=color.black;
		}
		return result;
	}

	public void deSerialize(color[] state){
		clearList();
		for(int i=0; i<state.length; i++){
			int index=getIndex(getSerializedRing(i), getSerializedPos(i));
			Box target=boxes.get(index);
			switch(state[i]){
			case black:
				target.setBlack();
				black.put(index,target);
				break;
			case empty:
				target.setFree();
				free.put(index, target);
				break;
			case white:
				target.setWhite();
				white.put(index,target);
				break;
			default:
				break;
			}
		}
	}

	private void clearList() {
		free.clear();
		white.clear();
		black.clear();
	}

	private int indexOf(Box b){
		return 8*b.getRing()+b.getPos();
	}

	private int getSerializedRing(int i){
		return i/8;
	}

	private int getSerializedPos(int i){
		return i%8;
	}

	public enum color{
		empty,white,black;
	}

	public boolean isTerminal(boolean turn, int piecesToPlace) {
		Map<Integer,Box> mine=(turn)?white:black;
		return piecesToPlace<=0 && (mine.size()<3 || getAvailableMoves(turn, piecesToPlace, true).size()==0);
	}

	public double getFinalValue(boolean turn) {
		return (turn)?-1000:1000;
	}
	
	public double getCutValue(int piecesToPlace){
		double result = 0;
		int trisCons = 8;
		int availableCons = 3;
		int pedineCons = 8;
		int countCons = 2;
		int pedineMie=getWhite();
		int pedineSue=getBlack();
		
		int whiteAvailableMoves = isWhitePhaseThree(piecesToPlace)?0:availableCons*getWhiteAvailableMoves();
		int blackAvailableMoves = isBlackPhaseThree(piecesToPlace)?0:availableCons*getBlackAvailableMoves();
		
		int countTLMie = getCountTL(true);
		int countTLSue = getCountTL(false);
		
		result+=whiteAvailableMoves+pedineCons*pedineMie+trisCons*getWhiteNumberOfTris()+countTLMie*countCons;
		result-=blackAvailableMoves+pedineCons*pedineSue+trisCons*getBlackNumberOfTris()+countTLSue*countCons;
		return result;
	}

}
