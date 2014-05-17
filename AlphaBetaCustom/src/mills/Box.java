package mills;

import java.util.List;

import search.IAction;

public abstract class Box {
	
	protected int ring;
	protected int pos;
	
	protected Boolean isOnTris=null;
	protected boolean black=false;
	protected boolean white=false;
	
	protected List<IAction> availableMoves;
	
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
	
	public List<IAction> getPossibleMovements(){
		if(availableMoves==null)
			generatePossibleMovements();
		return availableMoves;
	}
	protected abstract void generatePossibleMovements();
	
	protected abstract Box up();
	protected abstract Box down();
	protected abstract Box left();
	protected abstract Box right();
	
	protected abstract void setUp(Box newUp);
	protected abstract void setDown(Box newDown);
	protected abstract void setLeft(Box newLeft);
	protected abstract void setRight(Box newRight);
	
	
	protected boolean isFree(){
		return !white && !black;
	}
	
	@Override
	public abstract Box clone();

	@Override
	public String toString() {
		return "Box [ring=" + ring + ", pos=" + pos + ", isOnTris=" + isOnTris
				+ ", black=" + black + ", white=" + white + ", availableMoves="
				+ availableMoves + "]";
	}

	


}
