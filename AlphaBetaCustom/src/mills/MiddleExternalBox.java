package mills;

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

}
