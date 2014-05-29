package search;

import node.Node;

public class TimeoutAlphaBetaSearch {
	private final int minDepth=5;
	private int currentMaxDepth=minDepth;
	private Node root=null;
	private boolean stop=false;
	private IAction result=null;
	
	public IAction getNextMove(Node root, int maxSeconds) throws InterruptedException{
		this.root=root;
		this.stop=false;
		currentMaxDepth=currentMaxDepth-2;
		if(currentMaxDepth<minDepth)
			currentMaxDepth=minDepth;
		
		
		Thread t1 = new Thread(){
			private CachedAlphaBetaSearchAux searchEngine=new CachedAlphaBetaSearchAux();
			@Override
			public void run(){
				int temp=currentMaxDepth;
				result=searchEngine.getNextMove(TimeoutAlphaBetaSearch.this.root, minDepth);
				currentMaxDepth=((currentMaxDepth/2)<minDepth)?minDepth:currentMaxDepth/2;
				
				while(!stop)
				{
					result=searchEngine.getNextMove(TimeoutAlphaBetaSearch.this.root, temp);
					currentMaxDepth=temp;
					System.out.println("level "+currentMaxDepth+" done");
					temp++;

				}					
			}
		};
		t1.start();
		Thread.sleep(maxSeconds*1000);
		stop=true;
		t1.stop();
		
		return result;
	
		
	}
	
}