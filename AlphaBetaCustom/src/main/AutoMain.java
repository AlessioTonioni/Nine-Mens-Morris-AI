package main;

import graphic.Board;

import java.util.ArrayList;
import java.util.List;

import mills.MillsAction;
import node.Node;
import node.SimpleNode;
import node.TreeNode;
import search.IAction;
import search.TimeoutAlphaBetaSearch;
import state.MillsBoard;
import state.MillsBoard.color;
import state.MillsState;

public class AutoMain {
	public static void main(String[] args) throws InterruptedException {
		
		Board grafica=new Board(".");
		grafica.SetMove(-1, -1, 1, 1, -1, -1,"X");
		color[]  board={	color.empty,color.empty,color.empty,color.empty,color.empty,color.empty,color.empty,color.empty,
							color.empty,color.black,color.empty,color.empty,color.empty,color.empty,color.empty,color.empty,
							color.empty,color.empty,color.empty,color.empty,color.empty,color.empty,color.empty,color.empty};
		
		ArrayList<IAction> list = new ArrayList<IAction>();
		Node rootWhite=createInitialNode(false);
		Node rootBlack = createInitialNode(false,board);
		//Node root = createInitialNode(true);
		
		//CachedAlphaBetaSearch search = new CachedAlphaBetaSearch();
		//MinMaxSearch search=new MinMaxSearch();
		//AlphaBetaSearch search=new AlphaBetaSearch();
		TimeoutAlphaBetaSearch searchWhite=new TimeoutAlphaBetaSearch();
		TimeoutAlphaBetaSearch searchBlack=new TimeoutAlphaBetaSearch();
		
		MillsAction actionWhite =(MillsAction) searchWhite.getNextMove(rootWhite, 20);
		grafica.SetMove(actionWhite.getRingFrom(), actionWhite.getPosFrom(), actionWhite.getRingTo(), actionWhite.getPosTo(), actionWhite.getRingDelete(), actionWhite.getPosDelete(), "O");
		System.out.println(grafica.toString());
		
		MillsAction actionBlack =(MillsAction) searchBlack.getNextMove(rootBlack, 20);
		grafica.SetMove(actionBlack.getRingFrom(), actionBlack.getPosFrom(), actionBlack.getRingTo(), actionBlack.getPosTo(), actionBlack.getRingDelete(), actionBlack.getPosDelete(), "X");
		System.out.println(grafica.toString());
		list.clear();
		list.add(actionWhite);
		list.add(actionBlack);
		rootWhite = getSimpleNode(rootWhite, list);
		do
		{
			actionWhite =(MillsAction) searchWhite.getNextMove(rootWhite, 20);
			grafica.SetMove(actionWhite.getRingFrom(), actionWhite.getPosFrom(), actionWhite.getRingTo(), actionWhite.getPosTo(), actionWhite.getRingDelete(), actionWhite.getPosDelete(), "O");
			System.out.println(grafica.toString());
			list.clear();
			list.add(actionBlack);
			list.add(actionWhite);
			rootBlack = getSimpleNode(rootBlack, list);
			
			if(rootBlack.getState().isTerminal())
			{
				break;
			}
			
			actionBlack =(MillsAction) searchBlack.getNextMove(rootBlack, 20);
			grafica.SetMove(actionBlack.getRingFrom(), actionBlack.getPosFrom(), actionBlack.getRingTo(), actionBlack.getPosTo(), actionBlack.getRingDelete(), actionBlack.getPosDelete(), "X");
			System.out.println(grafica.toString());
			list.clear();
			list.add(actionWhite);
			list.add(actionBlack);
			rootWhite = getSimpleNode(rootWhite, list);
			
		}while(!rootWhite.getState().isTerminal());
		
		
	}
	
	private static Node createInitialNode(boolean isTreeEnabled){
		MillsState initialState=new MillsState(true,18);
		if(isTreeEnabled)
			return new TreeNode(initialState);
		else
			return new SimpleNode(initialState);
	}
	
	private static Node createInitialNode(boolean isTreeEnabled, color[] board){
		MillsState initialState=new MillsState(true,17,board,null);
		if(isTreeEnabled)
			return new TreeNode(initialState);
		else
			return new SimpleNode(initialState);
	}
	
	private static Node getSimpleNode(Node from, List<IAction> mosse){
		from.getState().restoreState();
		boolean turno=(from.getState().isMax())?true:false;
		for(IAction a:mosse){
			MillsBoard.getInstance().applyAction((MillsAction)a, turno);
			turno=!turno;
		}
		MillsState s=new MillsState(true, from.getState().getPiecesToPlace()-mosse.size());
		s.setState(MillsBoard.getInstance().serialize());
		return new SimpleNode(s,0);
	}
	
	private static Node getTreeNode(Node from, List<IAction> mosse){
		Node temp=from;
		for(IAction a:mosse){
			temp=temp.getRightSon(a);
		}
		temp.setFather(null);
		return temp;
	}
}
