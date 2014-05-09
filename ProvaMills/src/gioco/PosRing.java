package gioco;

public class PosRing implements Cloneable {

	private int XFrom;
	private int YFrom;

	private int XTo;
	private int YTo;

	private int XDelete;
	private int YDelete;
	
	public PosRing(int xFrom, int yFrom, int xTo, int yTo, int xDelete,
			int yDelete) {
		super();
		XFrom = xFrom;
		YFrom = yFrom;
		XTo = xTo;
		YTo = yTo;
		XDelete = xDelete;
		YDelete = yDelete;
	}
	public int getXFrom() {
		return XFrom;
	}
	public void setXFrom(int xFrom) {
		XFrom = xFrom;
	}
	public int getYFrom() {
		return YFrom;
	}
	public void setYFrom(int yFrom) {
		YFrom = yFrom;
	}
	public int getXTo() {
		return XTo;
	}
	public void setXTo(int xTo) {
		XTo = xTo;
	}
	public int getYTo() {
		return YTo;
	}
	public void setYTo(int yTo) {
		YTo = yTo;
	}
	public int getXDelete() {
		return XDelete;
	}
	public void setXDelete(int xDelete) {
		XDelete = xDelete;
	}
	public int getYDelete() {
		return YDelete;
	}
	public void setYDelete(int yDelete) {
		YDelete = yDelete;
	}
	@Override
	public PosRing clone(){
		PosRing result=new PosRing(XFrom,YFrom,XTo,YTo,XDelete,YDelete);
		return result;
	}
	@Override
	public String toString() {
		return "PosRing [XFrom=" + XFrom + ", YFrom=" + YFrom + ", XTo=" + XTo
				+ ", YTo=" + YTo + ", XDelete=" + XDelete + ", YDelete="
				+ YDelete + "]";
	}

}
