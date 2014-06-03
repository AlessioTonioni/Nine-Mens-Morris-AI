package search;

import java.util.List;

public interface IState {

	boolean isMin();
	boolean isMax();
	double getFinalValue();  //dovr� ritornarlo se gi� calcolato, calcolarlo altrimenti
	double getCutValue();
	boolean isTerminal();
	List<IState> getAvailableMoves();
	IAction getGeneratingMove();
	void restoreState();
	void applyAction();
	int getPiecesToPlace();
	void setPhase();
	int getPhase();
	
}
