package mills;

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
			availableMoves.add(new MillsAction(this.ring, this.pos, left.ring, left.pos, -1, -1));
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

}
