package search;

import java.util.List;

public interface IState {

	boolean isMin();
	boolean isMax();
	double getValue();  //dovrà ritornarlo se già calcolato, calcolarlo altrimenti
	void setValue(double value);
	boolean isTerminal();
	List<IState> getAvailableMoves();
	IAction getGeneratingMove();

}
