package mills;

import java.util.Map;

import prova.MillsBoard;

public class MiddleExternalBox extends Box {

	protected Box left;
	protected Box right;
	protected Box down;
	
	protected MiddleExternalBox(int ring, int pos) {
		super(ring, pos);
	}

	@Override
	protected void calculateTris() {
		if(black)
			isOnTris=((down.black && down.down().black) || (left.black && right.black));
		else if(white)
			isOnTris=((down.white && down.down().white) || (left.white && right.white));	
	}

	@Override
	protected void generatePossibleMovements() {
		if(left.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring, left.pos, -1, -1));
		if(right.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring, left.pos, -1, -1));
		if(down.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, down.ring, down.pos, -1, -1));
	}

	@Override
	protected Box up() {
		return null;
	}

	@Override
	protected Box down() {
		return down;
	}

	@Override
	protected Box left() {
		return left;
	}

	@Override
	protected Box right() {
		return right;
	}

	@Override
	protected void setUp(Box newUp) {
		return;
	}

	@Override
	protected void setDown(Box newDown) {
		this.down=newDown;
	}

	@Override
	protected void setLeft(Box newLeft) {
		this.left=newLeft;
	}

	@Override
	protected void setRight(Box newRight) {
		this.right=newRight;
	}

	@Override
	public Box clone() {
		Box result=new MiddleExternalBox(this.ring, this.pos);
		result.white=white;
		result.black=black;
		result.isOnTris=isOnTris;
		return result;
	}

	@Override
	public void connect(Map<Integer, Box> free) {
		this.left = free.get(MillsBoard.getIndex(ring, (pos+7)%8));
		this.right = free.get(MillsBoard.getIndex(ring, (pos+1)%8));
		this.down = free.get(MillsBoard.getIndex(1, pos));	
	}

	@Override
	protected void updateReferences() {
		down.reset();
		down.down().reset();
		left.reset();
		right.reset();
		
		left.resetMoves();
		right.resetMoves();
		down.resetMoves();
	}

}
