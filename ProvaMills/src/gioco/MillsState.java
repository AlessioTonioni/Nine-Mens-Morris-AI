package gioco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MillsState implements Cloneable{
	private static final int ROW=3;
	private static final int COLUMN=8;

	private static final int VUOTO=0;

	private int[][] board;
	private int pedineDisponibili;
	private int pedineMin;
	private int pedineMax;

	private double minMaxValue;

	private int turno;

	private int n;
	
	private List<PosRing> prevMoves;

	public int getTurno() {
		return turno;
	}

	public MillsState(int[][] board, int pedineMin, int pedineMax, int pedineDisponibili, int turno, int n, List<PosRing> prevMoves){
		this.turno=turno;
		this.n=n;
		this.board=board;
		this.pedineDisponibili=pedineDisponibili;
		this.pedineMin=pedineMin;
		this.pedineMax=pedineMax;
		this.prevMoves = prevMoves;
	}

	public MillsState(int turno, int n){
		this.turno=turno;
		this.board=new int[ROW][COLUMN];
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COLUMN; j++)
				board[i][j]=VUOTO;
		pedineDisponibili=18;
		pedineMin=0;
		pedineMax=0;
		this.n=n;
		this.prevMoves = new ArrayList<PosRing>();
	}
	
	public void reset(int n) {
		this.n = n;
	}

	public List<PosRing> getNextMoves() {
		if(isFaseUno())
			return getNextMovesFirst();
		else if((pedineMin>3 && turno==-1) || (pedineMax>3 && turno==1))
			return GetNextMovesSecond();
		else 
			return GetNextMovesThird();
	}

	private List<PosRing> getNextMovesFirst() {
		List<PosRing> result=new ArrayList<PosRing>();
		generateWarpFirst(-1,-1,result);
		return result;
	}

	private List<PosRing> GetNextMovesSecond() {
		List<PosRing> result=new ArrayList<PosRing>();
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COLUMN; j++){
				if(board[i][j]==turno){ //mia pedina
					generateAvailableMoves(i,j,result);
				}
			}
		return result;
	}

	private List<PosRing> GetNextMovesThird() {
		List<PosRing> result=new ArrayList<PosRing>();
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COLUMN; j++){
				if(board[i][j]==turno){ //mia pedina
					generateWarpThird(i,j,result);
				}
			}
		return result;
	}

	private boolean checkMills(int ring, int pos, int self) {
		if(pos%2==0){
			return(board[ring][(pos+1)%COLUMN]==self && board[ring][(pos+2)%COLUMN]==self) ||
					(board[ring][(pos+COLUMN-1)%COLUMN]==self && board[ring][(pos+COLUMN-2)%COLUMN]==self);
		} else {
			return(board[ring][(pos+1)%COLUMN]==self && board[ring][(pos+COLUMN-1)%COLUMN]==self) ||
					(board[(ring+1)%ROW][pos]==self && board[(ring+2)%ROW][pos]==self);		
		}
	}
	
	private boolean checkMills(int ringFrom, int posFrom, int ring, int pos, int self) {
		board[ringFrom][posFrom] = VUOTO;
		boolean result = checkMills(ring, pos, self);
		board[ringFrom][posFrom] = self;
		
		return result;
	}
	
	private void generateDeletables(PosRing temp, List<PosRing> result) {
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COLUMN; j++){
				if(board[i][j]==-turno && !checkMills(i,j, board[i][j])){
					PosRing next=temp.clone();
					next.setXDelete(i);
					next.setYDelete(j);
					result.add(next);
				}
			}
	}
	private void generateAvailableMoves(int row, int pos, List<PosRing> result) {
		moveRing(row,pos,result);
		if(pos%2!=0){  //devo controllare anche i tris inter-ring...
			if(row==0)
				generateMoveDown(row,pos,result);
			else if(row==2)
				generateMoveUp(row,pos,result);
			else if(row==1){
				generateMoveDown(row,pos,result);
				generateMoveUp(row,pos,result);
			}
		}
	}
	private void moveRing(int row, int pos, List<PosRing> result) {
		if(board[row][(pos+1)%COLUMN]==VUOTO){
			PosRing temp=new PosRing(row,pos,row,(pos+1)%COLUMN,-1,-1);
			if(checkMills(row, pos, row,(pos+1)%COLUMN,turno)){   //se la mossa genera un tris
				generateDeletables(temp,result);
			} else {   //mossa senza mangiata
				result.add(temp);
			}
		}

		if(board[row][(pos+COLUMN-1)%COLUMN]==VUOTO){
			PosRing temp=new PosRing(row,pos,row,(pos+COLUMN-1)%COLUMN,-1,-1);	
			if(checkMills(row, pos, row,(pos+COLUMN-1)%COLUMN,turno)){   //se la mossa genera un tris
				generateDeletables(temp,result);
			} else {   //mossa senza mangiata		
				result.add(temp);
			}
		}
	}
	private void generateMoveUp(int row, int pos, List<PosRing> result) {
		if(board[(row-1)%ROW][pos] == VUOTO) {
			PosRing temp=new PosRing(row,pos,(row-1)%ROW, pos,-1,-1);

			if(checkMills(row, pos, (row-1)%ROW,pos,turno)){   //se la mossa genera un tris
				generateDeletables(temp,result);
			} else {   //mossa senza mangiata		
				result.add(temp);
			}
		}
	}
	private void generateMoveDown(int row, int pos, List<PosRing> result) {
		if(board[(row+1)%ROW][pos] == VUOTO) {
			PosRing temp=new PosRing(row,pos,(row+1)%ROW, pos,-1,-1);

			if(checkMills(row, pos, (row+1)%ROW,pos,turno)){   //se la mossa genera un tris
				generateDeletables(temp,result);
			} else {   //mossa senza mangiata		
				result.add(temp);
			}
		}

	}
	private void generateWarpFirst(int rowFrom,int posFrom,List<PosRing> result){
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COLUMN; j++){
				if(board[i][j]==VUOTO){
					PosRing temp=new PosRing(rowFrom,posFrom,i,j,-1,-1);

					if(checkMills(i,j, turno)){
						generateDeletables(temp,result);
					} else {
						result.add(temp);
					}
				}
			}
	}
	private void generateWarpThird(int rowFrom,int posFrom,List<PosRing> result){
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COLUMN; j++){
				if(board[i][j]==VUOTO){
					PosRing temp=new PosRing(rowFrom,posFrom,i,j,-1,-1);

					if(checkMills(rowFrom, posFrom, i,j, turno)){
						generateDeletables(temp,result);
					} else {
						result.add(temp);
					}
				}
			}
	}

	public void movePiece(PosRing action){
		board[action.getXTo()][action.getYTo()]=turno; //pedine spostata o warpata

		if(action.getXFrom()!=-1 && action.getYFrom()!=-1)
			board[action.getXFrom()][action.getYFrom()]=VUOTO; //spostamento
		else{
			pedineDisponibili--; //warp fase 1
			if(pedineDisponibili>=0){
				if(turno==-1)
					pedineMin++;
				else
					pedineMax++;
			}
		}

		if(action.getXDelete()!=-1 && action.getYDelete()!=-1){ //devo cancellare qualcosa
			board[action.getXDelete()][action.getYDelete()]=VUOTO;
			if(turno==1)
				pedineMin--;
			else 
				pedineMax--;
		}

		turno=-turno;
		n=n-1;
		prevMoves.add(action);
		if(prevMoves.size() > 12) {
			prevMoves.remove(0);
		}
	}

	public MillsState performAction(PosRing action) {
		MillsState newState=this.clone();
		newState.movePiece(action);
		return newState;
	}

	public boolean checkWin(){
		if(turno==1)
			return pedineMin<3;
		else
			return pedineMax<3;
	}

	public double getUtility(){
		if(pedineMin<3 && isFaseDue()) {
			return 1000;
		} else if(pedineMax<3 && isFaseDue()) {
			return -1000;
		} else if(isPatta()){
			return 0;
		}
		
		getEuristica();
		return turno*minMaxValue;
	}

	private void getEuristica() {
		double result=0;
		int myTrisCons=5;
		int advTrisCons = 5;
		int availableCons = 3;
		int pedineCons = 10;
		int pedineMie=(turno==1)?pedineMax:pedineMin;
		int pedineSue=(turno!=1)?pedineMax:pedineMin;
		result+=availableCons*availableMoves(turno)+pedineCons*pedineMie+myTrisCons*numberOfMills(turno);
		result-=availableCons*availableMoves(-turno)+pedineCons*pedineSue+advTrisCons*numberOfMills(-turno);
		minMaxValue=result;
		//minMaxValue=10;
	}

	public MillsState clone(){
		int[][] newBoard=new int[ROW][COLUMN];
		for(int i=0; i<ROW; i++)
			newBoard[i]=Arrays.copyOf(board[i], board[i].length);
		List<PosRing> copyList = new ArrayList<PosRing>();
		for(PosRing pos : prevMoves) {
			copyList.add(pos);
		}
		return new MillsState(newBoard, pedineMin,pedineMax,pedineDisponibili, turno,n, copyList);
	}

	public boolean isTerminal() {
		boolean a = (n<=0 || (pedineMin<3 && isFaseDue()) || (pedineMax<3 && isFaseDue()) || isPatta() 
				//|| (availableMoves(-turno) == 0 && pedineDisponibili<0) || (availableMoves(turno) == 0 && pedineDisponibili<0)
				);
		return a;
	}

	private boolean isPatta() {
		if(isFaseUno()){
			return false;
		}
		boolean result= prevMoves.get(0).equals(prevMoves.get(4)) && prevMoves.get(0).equals(prevMoves.get(8));
		result &=  prevMoves.get(1).equals(prevMoves.get(5)) && prevMoves.get(1).equals(prevMoves.get(9));
		return result;
	}

	private int availableMoves(int mioTurno){
		List<PosRing> res=new ArrayList<PosRing>();
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COLUMN; j++){
				if(board[i][j]==mioTurno)
					generateAvailableMoves(i,j,res);
			}
		return res.size();
	}
	
	private int numberOfMills(int mioTurno){
		int result=0;
		result+=(checkMills(0,0,mioTurno))?1:0;
		result+=(checkMills(0,4,mioTurno))?1:0;
		result+=(checkMills(1,1,mioTurno))?1:0;
		result+=(checkMills(1,3,mioTurno))?1:0;
		result+=(checkMills(1,5,mioTurno))?1:0;
		result+=(checkMills(1,7,mioTurno))?1:0;
		result+=(checkMills(2,0,mioTurno))?1:0;
		result+=(checkMills(2,4,mioTurno))?1:0;
		return result;
	}

	private boolean isFaseDue(){
		return pedineDisponibili<=0;
	}
	
	private boolean isFaseUno(){
		return pedineDisponibili>0;
	}
	
	private boolean isFaseTre(){
		return (pedineDisponibili<=0 && pedineMax<3 || pedineDisponibili<=0 && pedineMin<3);
	}
	
	@Override
	public String toString() {
		String temp="";
		for(int i=0; i<ROW; i++){
			temp+="[";
			for(int j=0; j<COLUMN; j++){
				temp+=" "+board[i][j];
			}
			temp+="] ";
		}
		return "MillsState [board=" + temp
				+ ", pedineDisponibili=" + pedineDisponibili + ", pedineMin="
				+ pedineMin + ", pedineMax=" + pedineMax + ", minMaxValue="
				+ minMaxValue + ", turno=" + turno + ", n=" + n + "]";
	}

	public MillsState reverse() {
		int[][] newBoard = new int[ROW][COLUMN];
		for(int i=0; i<ROW; i++) {
			for(int j=0; j<COLUMN; j++) {
				newBoard[i][j] = -board[i][j];
			}
		}
		return new MillsState(newBoard, pedineMax, pedineMin, pedineDisponibili, -turno, n, prevMoves);
	}

}
