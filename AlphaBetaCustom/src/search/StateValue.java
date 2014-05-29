package search;

public class StateValue {

	private boolean terminal;
	private boolean valid;
	private double value;
	private int lastExpanded;
	
	public StateValue(boolean terminal, boolean valid, double value, int lastExpanded) {
		this.terminal = terminal;
		this.valid = valid;
		this.value = value;
		this.lastExpanded = lastExpanded;
	}
	
	public boolean isTerminal() {
		return terminal;
	}
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getLastExpanded() {
		return lastExpanded;
	}
	public void setLastExpanded(int lastExpanded) {
		this.lastExpanded = lastExpanded;
	}
	
}
