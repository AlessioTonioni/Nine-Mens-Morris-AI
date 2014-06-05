package mills;

import java.util.Map;

import stato.MillsBoard;

public class CentralBox extends Box {

	protected Box left;
	protected Box right;
	protected Box up;
	protected Box down;
	
	protected CentralBox(int ring, int pos) {
		super(ring, pos);
	}

	@Override
	protected void calculateTris() {
		if(black)
			isOnTris=((left.black && right.black) || (up.black && down.black));
		else if(white)
			isOnTris=((left.white && right.white) || (up.white && down.white));
	}

	@Override
	protected void generatePossibleMovements() {
		if(left.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring, left.pos, -1, -1));
		if(right.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, right.ring, right.pos, -1, -1));
		if(up.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, up.ring, up.pos, -1, -1));
		if(down.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, down.ring, down.pos, -1, -1));
	}

	@Override
	protected Box up() {
		return up;
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
		up=newUp;
	}

	@Override
	protected void setDown(Box newDown) {
		down=newDown;	
	}

	@Override
	protected void setLeft(Box newLeft) {
		left=newLeft;
	}

	@Override
	protected void setRight(Box newRight) {
		right=newRight;
	}

	@Override
	public Box clone() {
		Box result=new CentralBox(this.ring,this.pos);
		result.black=this.black;
		result.white=this.white;
		result.isOnTris=isOnTris;
		return result;
	}

	@Override
	public void connect(Map<Integer, Box> free) {
		this.left = free.get(MillsBoard.getIndex(ring, (pos+7)%8));
		this.right = free.get(MillsBoard.getIndex(ring, (pos+1)%8));
		this.up = free.get(MillsBoard.getIndex(0, pos));
		this.down = free.get(MillsBoard.getIndex(2, pos));		
	}

	@Override
	protected void updateReferences() {
		reset();
		left.reset();
		right.reset();
		up.reset();
		down.reset();
		resetMoves();
		left.resetMoves();
		right.resetMoves();
		up.resetMoves();
		down.resetMoves();
	}

	@Override
	public int countTL(boolean turn) {
		int res = 0;
		if (isFree()) {
			if (up.isMyColor(turn))
				res++;
			if (down.isMyColor(turn))
				res++;
			if (right.isMyColor(turn))
				res++;
			if (left.isMyColor(turn))
				res++;
		}
		if (res >= 3) {
			return res - 2;
		}
		return 0;
	}
}
