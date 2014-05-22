package graphic;

public class Board {
	String[][] board=new String[3][8];
	String vuoto=".";
	
	
	
	public Board(String vuoto) {
		this.vuoto=vuoto;
		for(int i=0; i<3; i++)
			for(int j=0; j<8; j++)
				board[i][j]=vuoto;
	}
	
	public String SetMove(int fromROW, int fromCOL, int toROW, int toCOL, int removeROW, int removeCOL, String who)
	{
		if(fromROW!=-1 && fromCOL !=-1)board[fromROW][fromCOL]=vuoto;
		board[toROW][toCOL]=who;
		if(removeROW!=-1 && removeCOL !=-1)board[removeROW][removeCOL]=vuoto;
		return this.toString();
	}




	@Override
	public String toString() {
		String result="" ;
		result+= board[0][0]+"-----"+board[0][1]+"-----"+board[0][2]											+"\n";
		result+= "| "+board[1][0]+"---"+board[1][1]+"---"+board[1][2]+" |"										+"\n";
		result+= "| "+"| "+board[2][0]+"-"+board[2][1]+"-"+board[2][2]+" |"+" |"								+"\n";
		result+= board[0][7]+"-"+board[1][7]+"-"+board[2][7]+"   "+board[2][3]+"-"+board[1][3]+"-"+board[0][3]	+"\n";
		result+= "| "+"| "+board[2][6]+"-"+board[2][5]+"-"+board[2][4]+" |"+" |"								+"\n";
		result+= "| "+board[1][6]+"---"+board[1][5]+"---"+board[1][4]+" |"										+"\n";
		result+= board[0][6]+"-----"+board[0][5]+"-----"+board[0][4]											+"\n";
		result+="\n";
		return result;
	}
	

}
