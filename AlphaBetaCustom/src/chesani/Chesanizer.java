package chesani;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;

import java.util.HashMap;
import java.util.Map;

import mills.MillsAction;
import node.Node;
import node.SimpleNode;
import stato.MillsState;
import stato.MillsBoard.color;

public class Chesanizer {
	private Map<Integer, String> chesanizer;
	private boolean white;

	public Chesanizer(boolean white) {
		chesanizer = new HashMap<Integer, String>();
		
		//first ring
		chesanizer.put(0, "a7");
		chesanizer.put(1, "d7");
		chesanizer.put(2, "g7");
		chesanizer.put(3, "g4");
		chesanizer.put(4, "g1");
		chesanizer.put(5, "d1");
		chesanizer.put(6, "a1");
		chesanizer.put(7, "a4");

		//second ring
		chesanizer.put(8, "b6");
		chesanizer.put(9, "d6");
		chesanizer.put(10, "f6");
		chesanizer.put(11, "f4");
		chesanizer.put(12, "f2");
		chesanizer.put(13, "d2");
		chesanizer.put(14, "b2");
		chesanizer.put(15, "b4");

		//third ring
		chesanizer.put(16, "c5");
		chesanizer.put(17, "d5");
		chesanizer.put(18, "e5");
		chesanizer.put(19, "e4");
		chesanizer.put(20, "e3");
		chesanizer.put(21, "d3");
		chesanizer.put(22, "c3");
		chesanizer.put(23, "c4");

		this.white = white;
	}

	public String chesanize(int value) {
		return chesanizer.get(value);
	}

	public String chesanize(int ring, int pos) {
		return chesanize(8*ring+pos);
	}

	public Node deChesanizer(State s) {
		color[] res = new color[24];
		HashMap<String, Checker> map = s.getBoard();

		for(int i=0;i<24;i++) {
			res[i] = translate(map.get(chesanizer.get(i)));
		}

		System.out.println("Piecestoplace: "+s.getWhiteCheckers()+s.getBlackCheckers());
		return createInitialNode(s.getWhiteCheckers()+s.getBlackCheckers(), res);
	}

	private color translate(Checker checker) {
		if(white) {
			switch(checker) {
			case BLACK: return color.black;
			case EMPTY: return color.empty;
			case WHITE: return color.white;
			}
		} else {
			switch(checker) {
			case BLACK: return color.white;
			case EMPTY: return color.empty;
			case WHITE: return color.black;
			}
		}
		return null;
	}

	private Node createInitialNode(int piecesToPlace, color[] board){
		MillsState initialState=new MillsState(true,piecesToPlace,board,null);
		return new SimpleNode(initialState);
	}
	
	public Action chesanizeAction(MillsAction a, int phase) {
		switch(phase) {
		case 1: { Phase1Action action = new Phase1Action();
				  action.setPutPosition(chesanize(a.getRingTo(), a.getPosTo()));
				  if(a.getRingDelete() != -1) {
					  action.setRemoveOpponentChecker(chesanize(a.getRingDelete(), a.getPosDelete()));
				  }
				  return action;
				}
		case 2: { Phase2Action action = new Phase2Action();
				  action.setFrom(chesanize(a.getRingFrom(), a.getPosFrom()));
				  action.setTo(chesanize(a.getRingTo(), a.getPosTo()));
				  if(a.getRingDelete() != -1) {
					  action.setRemoveOpponentChecker(chesanize(a.getRingDelete(), a.getPosDelete()));
				  }
				  return action;
				}
		case 3: { PhaseFinalAction action = new PhaseFinalAction();
				  action.setFrom(chesanize(a.getRingFrom(), a.getPosFrom()));
				  action.setTo(chesanize(a.getRingTo(), a.getPosTo()));
				  if(a.getRingDelete() != -1) {
					  action.setRemoveOpponentChecker(chesanize(a.getRingDelete(), a.getPosDelete()));
				  }
				  return action;
				}
		}
		return null;
	}
}
