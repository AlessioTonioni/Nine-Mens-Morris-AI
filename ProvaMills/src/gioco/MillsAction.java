package gioco;

public class MillsAction implements Cloneable {

	private int XFrom;
	private int YFrom;

	private int XTo;
	private int YTo;

	private int XDelete;
	private int YDelete;
	
	public MillsAction(int xFrom, int yFrom, int xTo, int yTo, int xDelete,
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
	public MillsAction clone(){
		MillsAction result=new MillsAction(XFrom,YFrom,XTo,YTo,XDelete,YDelete);
		return result;
	}
	@Override
	public String toString() {
		/*
		return "PosRing [XFrom=" + XFrom + ", YFrom=" + YFrom + ", XTo=" + XTo
				+ ", YTo=" + YTo + ", XDelete=" + XDelete + ", YDelete="
				+ YDelete + "]";
		*/
		return "PosRing ["
				+(XFrom>=0 ? "XFrom=" + XFrom +", " : "")
				+(YFrom>=0 ? "YFrom=" + YFrom +", " : "")
				+(XTo>=0 ? "XTo=" + XTo +", " : "")
				+(YTo>=0 ? "YTo=" + YTo +", " : "")
				+(XDelete>=0 ? "XDelete=" + XDelete +", " : "")
				+(YDelete>=0 ? "YDelete=" + YDelete +", " : "")+"]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + XDelete;
		result = prime * result + XFrom;
		result = prime * result + XTo;
		result = prime * result + YDelete;
		result = prime * result + YFrom;
		result = prime * result + YTo;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MillsAction other = (MillsAction) obj;
		if (XDelete != other.XDelete)
			return false;
		if (XFrom != other.XFrom)
			return false;
		if (XTo != other.XTo)
			return false;
		if (YDelete != other.YDelete)
			return false;
		if (YFrom != other.YFrom)
			return false;
		if (YTo != other.YTo)
			return false;
		return true;
	}

	
}
