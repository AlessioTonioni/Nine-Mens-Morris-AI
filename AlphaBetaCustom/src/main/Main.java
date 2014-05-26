package main;

import graphic.Board;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import mills.MillsAction;
import node.Node;
import node.SimpleNode;
import node.TreeNode;
import prova.MillsBoard;
import prova.MillsBoard.color;
import prova.MillsState;
import search.AlphaBetaSearch;
import search.CachedAlphaBetaSearch;
import search.IAction;
import search.MinMaxSearch;
import search.TimeoutAlphaBetaSearch;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		
		Board grafica=new Board(".");
		//grafica.SetMove(-1, -1, 1, 1, -1, -1,"X");
		//color[]  board={	color.black,color.black,color.empty,color.black,color.white,color.white,color.black,color.black,
		//					color.white,color.empty,color.empty,color.empty,color.empty,color.black,color.empty,color.empty,
		//					color.white,color.empty,color.black,color.black,color.empty,color.empty,color.empty,color.white};
		
		Node root=createInitialNode(false);
		//Node root = createInitialNode(false,board);
		//Node root = createInitialNode(true);
		root.getState().restoreState();
		if(root.getState().isTerminal()){
			System.out.println("bababab");
		}
		//CachedAlphaBetaSearch search = new CachedAlphaBetaSearch();
		//MinMaxSearch search=new MinMaxSearch();
		//AlphaBetaSearch search=new AlphaBetaSearch();
		TimeoutAlphaBetaSearch search=new TimeoutAlphaBetaSearch();
		MillsAction action =(MillsAction) search.getNextMove(root, 30);
		
		grafica.SetMove(action.getRingFrom(), action.getPosFrom(), action.getRingTo(), action.getPosTo(), action.getRingDelete(), action.getPosDelete(), "O");
		System.out.println("Action: "+action);
		System.out.println(grafica.toString());
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		do{
			System.out.println("Inserisci prossima mossa: ");
			try {
				String line = in.readLine();
				StringTokenizer st = new StringTokenizer(line);
				int ringFrom = Integer.parseInt(st.nextToken());
				int posFrom = Integer.parseInt(st.nextToken());
				int ringTo = Integer.parseInt(st.nextToken());
				int posTo = Integer.parseInt(st.nextToken());
				int ringDelete = Integer.parseInt(st.nextToken());
				int posDelete = Integer.parseInt(st.nextToken());
				MillsAction posring = new MillsAction(ringFrom, posFrom, ringTo, posTo, ringDelete, posDelete);
				grafica.SetMove(posring.getRingFrom(), posring.getPosFrom(), posring.getRingTo(), posring.getPosTo(), posring.getRingDelete(), posring.getPosDelete(), "X");
				System.out.println(grafica.toString());
				List<IAction> movesList=new ArrayList<IAction>();
				movesList.add(action);
				movesList.add(posring);
				
				root=getSimpleNode(root, movesList);
				//root=getTreeNode(root,movesList);
				
				System.out.println("Inserire profonditÓ: ");
				line=in.readLine();
				action=(MillsAction) search.getNextMove(root, Integer.parseInt(line));
				grafica.SetMove(action.getRingFrom(), action.getPosFrom(), action.getRingTo(), action.getPosTo(), action.getRingDelete(), action.getPosDelete(), "O");
				System.out.println(action);
				System.out.println(grafica.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		} while(!root.getState().isTerminal());
		System.out.println("Nodo terminale.");
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
