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
		currentMaxDepth=currentMaxDepth-3;
		if(currentMaxDepth<minDepth)
			currentMaxDepth=minDepth;
		
		
		Thread t1 = new Thread(){
			private CachedAlphaBetaSearch searchEngine=new CachedAlphaBetaSearch();
			@Override
			public void run(){
				while(!stop)
				{
					currentMaxDepth++;
					result=searchEngine.getNextMove(TimeoutAlphaBetaSearch.this.root, currentMaxDepth);
					System.out.println("level "+currentMaxDepth+" done");
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
