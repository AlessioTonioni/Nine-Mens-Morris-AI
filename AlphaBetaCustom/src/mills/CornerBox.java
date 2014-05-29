package mills;

import java.util.Map;

import prova.MillsBoard;

public class CornerBox extends Box {

	protected Box left;
	protected Box right;

	public CornerBox(int row, int pos) {
		super(row, pos);
	}

	@Override
	protected void calculateTris() {
		if (black)
			isOnTris = (left.black && left.left().black)
					|| (right.black && right.right().black);
		else if (white)
			isOnTris = (left.white && left.left().white)
					|| (right.white && right.right().white);
	}

	@Override
	protected void generatePossibleMovements() {
		if (left.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring,
					left.pos, -1, -1));
		if (right.isFree())
			availableMoves.add(new MillsAction(this.ring, this.pos, right.ring,
					right.pos, -1, -1));
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

	@Override
	public void setLeft(Box left) {
		this.left = left;
	}

	@Override
	public void setRight(Box right) {
		this.right = right;
	}

	@Override
	protected void setUp(Box newUp) {
		return;
	}

	@Override
	protected void setDown(Box newDown) {
		return;
	}

	@Override
	public Box clone() {
		Box result = new CornerBox(this.ring, this.pos);
		result.white = white;
		result.black = black;
		result.isOnTris = isOnTris;
		return result;
	}

	@Override
	public void connect(Map<Integer, Box> free) {
		this.left = free.get(MillsBoard.getIndex(ring, (pos + 7) % 8));
		this.right = free.get(MillsBoard.getIndex(ring, (pos + 1) % 8));
	}

	@Override
	protected void updateReferences() {
		reset();
		left.reset();
		left.left().reset();
		right.reset();
		right.right().reset();
		resetMoves();
		left.resetMoves();
		right.resetMoves();
	}

	@Override
	public int countTL(boolean turn) {
		int res = 0;
		if (isFree()) {
			if (right.right().isMyColor(turn))
				res++;
			if (left.left().isMyColor(turn))
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
