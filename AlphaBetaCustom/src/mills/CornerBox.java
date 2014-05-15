package mills;


public class CornerBox extends Box {

	protected Box left;
	protected Box right;
	
	public CornerBox(int row, int pos){
		super(row,pos);
	}

	@Override
	protected void calculateTris() {
		if(black)
			isOnTris=(left.black && left.left().black) || (right.black && right.right().black);
		else if(white)
			isOnTris=(left.white && left.left().white) || (right.white && right.right().white);	
	}

	@Override
	protected void generatePossibleMovements() {
		if(left.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring, left.pos, -1, -1));
		if(right.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring, left.pos, -1, -1));	
	}

	@Override
	protected Box up() {
		return null;
	}

	@Override
	protected Box down() {
		return null;
	}

	@Override
	protected Box left() {
		return left;
	}

	@Override
	protected Box right() {
		return right;
	}
	
	public void setLeft(Box left){
		this.left=left;
	}
	
	public void setRight(Box right){
		this.right=right;
	}

}
