package mills;

import search.IAction;

public class MillsAction implements IAction {

	private int ringFrom;
	private int posFrom;

	private int ringTo;
	private int posTo;

	private int ringDelete;
	private int posDelete;
	

	public MillsAction(int ringFrom, int posFrom, int ringTo, int posTo,
			int ringDelete, int posDelete) {
		super();
		this.ringFrom = ringFrom;
		this.posFrom = posFrom;
		this.ringTo = ringTo;
		this.posTo = posTo;
		this.ringDelete = ringDelete;
		this.posDelete = posDelete;
	}


	public int getRingFrom() {
		return ringFrom;
	}


	public int getPosFrom() {
		return posFrom;
	}


	public int getRingTo() {
		return ringTo;
	}


	public int getPosTo() {
		return posTo;
	}


	public int getRingDelete() {
		return ringDelete;
	}


	public void setRingDelete(int ringDelete) {
		this.ringDelete = ringDelete;
	}


	public void setPosDelete(int posDelete) {
		this.posDelete = posDelete;
	}


	public int getPosDelete() {
		return posDelete;
	}


	@Override
	public MillsAction clone(){
		MillsAction result=new MillsAction(ringFrom,posFrom,ringTo,posTo,ringDelete,posDelete);
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
				+(ringFrom>=0 ? "XFrom=" + ringFrom +", " : "")
				+(posFrom>=0 ? "YFrom=" + posFrom +", " : "")
				+(ringTo>=0 ? "XTo=" + ringTo +", " : "")
				+(posTo>=0 ? "YTo=" + posTo +", " : "")
				+(ringDelete>=0 ? "XDelete=" + ringDelete +", " : "")
				+(posDelete>=0 ? "YDelete=" + posDelete +", " : "")+"]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ringDelete;
		result = prime * result + ringFrom;
		result = prime * result + ringTo;
		result = prime * result + posDelete;
		result = prime * result + posFrom;
		result = prime * result + posTo;
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
		if (ringDelete != other.ringDelete)
			return false;
		if (ringFrom != other.ringFrom)
			return false;
		if (ringTo != other.ringTo)
			return false;
		if (posDelete != other.posDelete)
			return false;
		if (posFrom != other.posFrom)
			return false;
		if (posTo != other.posTo)
			return false;
		return true;
	}
	

}
