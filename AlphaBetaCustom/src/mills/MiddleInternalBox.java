package mills;

import java.util.Map;

import stato.MillsBoard;

public class MiddleInternalBox extends Box {

	protected Box left;
	protected Box right;
	protected Box up;

	protected MiddleInternalBox(int ring, int pos) {
		super(ring, pos);
	}

	@Override
	protected void calculateTris() {
		if(black)
			isOnTris=((up.black && up.up().black) || (left.black && right.black));
		else if(white)
			isOnTris=((up.white && up.up().white) || (left.white && right.white));	
	}

	@Override
	protected void generatePossibleMovements() {
		if(left.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring, left.pos, -1, -1));
		if(right.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, right.ring, right.pos, -1, -1));
		if(up.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, up.ring, up.pos, -1, -1));
	}

	@Override
	protected Box up() {
		return up;
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

	@Override
	protected void setUp(Box newUp) {
		this.up=newUp;
	}

	@Override
	protected void setDown(Box newDown) {
		return;
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
		Box result=new MiddleInternalBox(this.ring, this.pos);
		result.white=white;
		result.black=black;
		result.isOnTris=isOnTris;
		return result;
	}

	@Override
	public void connect(Map<Integer, Box> free) {
		this.left = free.get(MillsBoard.getIndex(ring, (pos+7)%8));
		this.right = free.get(MillsBoard.getIndex(ring, (pos+1)%8));
		this.up = free.get(MillsBoard.getIndex(1, pos));
	}

	@Override
	protected void updateReferences() {
		reset();
		up.reset();
		up.up().reset();
		left.reset();
		right.reset();
		resetMoves();
		left.resetMoves();
		right.resetMoves();
		up.resetMoves();
	}

	@Override
	public int countTL(boolean turn) {
		int res = 0;
		if (isFree()) {
			if (up.isMyColor(turn))
				res++;
			if (right.isMyColor(turn))
				res++;
			if (left.isMyColor(turn))
				res++;
		}
		if (res >= 3) {
			return 1;
		}
		return 0;
	}

}
