package mills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Box {
	
	protected int ring;
	protected int pos;
	protected Boolean isOnTris=null;
	protected boolean black=false;
	protected boolean white=false;
	
	protected abstract Box up();
	protected abstract Box down();
	protected abstract Box left();
	protected abstract Box right();
	
	protected abstract void setUp(Box newUp);
	protected abstract void setDown(Box newDown);
	protected abstract void setLeft(Box newLeft);
	protected abstract void setRight(Box newRight);
	
	protected List<MillsAction> availableMoves;
	
	protected abstract void updateReferences();
	
	public boolean isBlack() {
		return black;
	}

	public void setBlack() {
		this.black = true;
		this.white = false;
		updateReferences();
	}
	
	public void setFree(){
		this.black = false;
		this.white = false;
		updateReferences();
	}
	public boolean isWhite() {
		return white;
	}

	public void setWhite() {
		this.white = true;
		this.black = false;
		updateReferences();
	}

	public int getRing() {
		return ring;
	}

	public int getPos() {
		return pos;
	}
	
	protected Box(int ring, int pos){
		this.ring=ring;
		this.pos=pos;
	}
	
	public boolean isOnTris(){
		if(!black && !white)
			isOnTris=false;
		if(isOnTris==null)
			calculateTris();
		return isOnTris;
	}
	protected abstract void calculateTris();
	
	public List<MillsAction> getPossibleMovements(){
		if(availableMoves==null) {
			this.availableMoves = new ArrayList<MillsAction>();
			generatePossibleMovements();
		}
		return availableMoves;
	}
	
	protected abstract void generatePossibleMovements();	
	
	protected boolean isFree(){
		return !white && !black;
	}
	
	public static Box generateBox(int i, int j) {
		if(j%2 == 0) {
			return new CornerBox(i, j);
		} else if(i == 0) {
			return new MiddleExternalBox(i, j);
		} else if(i == 1) {
			return new CentralBox(i, j);
		} else {
			return new MiddleInternalBox(i, j);
		}
	}

	public abstract void connect(Map<Integer, Box> free);

	public void reset() {
		isOnTris = null;
	}
	
	public void resetMoves() {
		availableMoves = null;
	}

}
