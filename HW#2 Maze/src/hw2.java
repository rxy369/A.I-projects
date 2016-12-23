import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Comparator;

public class hw2 {
	
	Scanner myScanner = new Scanner(System.in);
	
	//holds 2 ints representing the current spot in the maze
	public class state{
		int row;
		int col;
		public state(){
			row = 0;
			col = 0;
		}
		public state(int r, int c){
			row = r;
			col = c;
		}
		@Override
		public boolean equals(Object obj){
			state temp = (state) obj;
			if(row == temp.row && col== temp.col){
				return true;
			}
			else{
				return false;
			}
		}
	}

	public class node{
		state curState;
		int depth;
		int totalCost;
		String steps;
		
		public node(){
			curState = new state(-1,-1);
			depth = 0;
			totalCost = 0;
			steps = "";
		}
		
		public node(state thisState){
			curState = thisState;
			depth = 0;
			totalCost = 0;
			steps = "";
		}
		
		
		/*
		public boolean equals(node n2){
			if(this.depth == n2.depth && this.totalCost == n2.totalCost && this.steps.equals(n2.steps) && this.curState.row == n2.curState.row && this.curState.col == n2.curState.col){
				return true;
		}
			else
				return false;
		}
		*/
	}
	
	
	
	public state findInit(char[][] maze){
		
		for(int r = 0; r < maze.length; r++){
			for(int c = 0; c < maze[r].length; c++){
				if(maze[r][c] == 'S'){
					return new state(r,c);
				}
			}
		}
		return new state(-1,-1);
	}
	
	public state findGoal(char[][] maze){
		
		for(int r = 0; r < maze.length; r++){
			for(int c = 0; c < maze[r].length; c++){
				if(maze[r][c] == 'F'){
					return new state(r,c);
				}
			}
		}
		return new state(-1,-1);
	}
	
	public boolean stateEquals(state s1, state s2){
		
		if((s1.row == s2.row) && s1.col == s2.col){
			return true;
		}
		else
			return false;
	}
	// Breadth first search
	public node bfs(char[][] maze){
		
		int depth = 0;
		int totalCost = 0;	
		state init = (findInit(maze));
		state goal = (findGoal(maze));
		node curNode = new node(init);
		LinkedList<node> frontier = new LinkedList<node>();
		ArrayList<state> explored = new ArrayList<state>();
		
		if(stateEquals(init,goal)){
			return new node(init);
		}
		else{
			frontier.add(curNode);
		}
		while(true){
			if(frontier.isEmpty()){
				//System.out.println("There is nothing in the frontier!!");
				return null;
			}
			curNode = frontier.pop(); //shallowest node
			explored.add(curNode.curState);
			LinkedList<node> newSpaces = findAdjSpaces(curNode,maze);
			for(node temp : newSpaces) {
				node child = temp;
				if(!(explored.contains(child.curState)) && !(frontier.contains(child))){
					if(stateEquals(child.curState,goal)){
						return child;
					}
					else{
						frontier.add(child);
					}
				}	
			}	
		}
	}
	
	public node dfs(char[][] maze){

		int depth = 0;
		int totalCost = 0;	
		state init = (findInit(maze));
		state goal = (findGoal(maze));
		node curNode = new node(init);
		ArrayDeque<node> frontier = new ArrayDeque<node>();
		ArrayList<state> explored = new ArrayList<state>();
		
		if(stateEquals(init,goal)){
			return new node(init);
		}
		else{
			frontier.add(curNode);
		}
		while(true){
			if(frontier.isEmpty()){
				//System.out.println("There is nothing in the frontier!!");
				return null;
			}
			curNode = frontier.pop(); //shallowest node
			explored.add(curNode.curState);
			LinkedList<node> newSpaces = findAdjSpaces(curNode,maze);
			for(node temp : newSpaces) {
				node child = temp;
				if(!(explored.contains(child.curState)) && !(frontier.contains(child))){
					if(stateEquals(child.curState,goal)){
						return child;
					}
					else{
						frontier.add(child);
					}
				}	
			}	
		}
	}
	
	
	public node ucs(char[][] maze){
		Comparator<node> change = new changeComparatorUCS();
		int depth = 0;
		int totalCost = 0;	
		state init = (findInit(maze));
		state goal = (findGoal(maze));
		node curNode = new node(init);
		PriorityQueue<node> frontier = new PriorityQueue<node>(change);
		ArrayList<state> explored = new ArrayList<state>();
		
		if(stateEquals(init,goal)){
			return new node(init);
		}
		else{
			frontier.add(curNode);
		}
		while(true){
			if(frontier.isEmpty()){
				//System.out.println("There is nothing in the frontier!!");
				return null;
			}
			curNode = frontier.remove(); //shallowest node
			explored.add(curNode.curState);
			LinkedList<node> newSpaces = findAdjSpaces(curNode,maze);
			for(node temp : newSpaces) {
				node child = temp;
				if(!(explored.contains(child.curState)) && !(frontier.contains(child))){
					if(stateEquals(child.curState,goal)){
						return child;
					}
					else{
						frontier.add(child);
					}
				}	
			}	
		}
	}
	
	private boolean isOnEdge(char[][] maze, node thisNode){
		if(thisNode.curState.row == maze.length-2 || thisNode.curState.col == maze[0].length-2
				|| thisNode.curState.row == 1 || thisNode.curState.col == 1){
			return true;
		}
		else
			return false;
	}
	
	private LinkedList<node> findAdjSpaces(node curNode, char[][] maze){
		
		LinkedList<node> valids = new LinkedList<node>(); 
		//north adjacent 
		if(maze[curNode.curState.row-1][curNode.curState.col] != '-'){ 
			node tempNode = new node(new state(curNode.curState.row-2,curNode.curState.col));
			tempNode.depth = curNode.depth +1;
			tempNode.totalCost = curNode.totalCost + 1;
			if(isOnEdge(maze,tempNode))
				tempNode.totalCost += 10;
			tempNode.steps = curNode.steps + "N";
			valids.add(tempNode); // creates a new node at 2 spaces north and adds it
		}
		//south
		if(maze[curNode.curState.row+1][curNode.curState.col] != '-'){ 
			node tempNode = new node(new state(curNode.curState.row+2,curNode.curState.col));
			tempNode.depth = curNode.depth +1;
			tempNode.totalCost = curNode.totalCost + 1;
			if(isOnEdge(maze,tempNode))
				tempNode.totalCost += 10;
			tempNode.steps = curNode.steps + "S";
			valids.add(tempNode); // creates a new node at 2 spaces north and adds it
		}
		//east
		if(maze[curNode.curState.row][curNode.curState.col+1] != '|'){ 
			node tempNode = new node(new state(curNode.curState.row,curNode.curState.col+2));
			tempNode.depth = curNode.depth +1;
			tempNode.totalCost = curNode.totalCost + 1;
			if(isOnEdge(maze,tempNode))
				tempNode.totalCost += 10;
			tempNode.steps = curNode.steps + "E";
			valids.add(tempNode); // creates a new node at 2 spaces north and adds it
		}
		//west
		if(maze[curNode.curState.row][curNode.curState.col-1] != '|'){ 
			node tempNode = new node(new state(curNode.curState.row,curNode.curState.col-2));
			tempNode.depth = curNode.depth +1;
			tempNode.totalCost = curNode.totalCost + 1;
			if(isOnEdge(maze,tempNode))
				tempNode.totalCost += 10;
			tempNode.steps = curNode.steps + "W";
			valids.add(tempNode); // creates a new node at 2 spaces north and adds it
		}
		return valids;
	}
	
	
	
	public static void main(String[] args) {
		hw2 hw2Instance = new hw2();
		hw2Instance.Main(args);
	}

	public void Main(String[] args){

		
		String myFile = args[0];
		Scanner myScanner = new Scanner("");
		
		try {
			myScanner = new Scanner(new File(myFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		int numRows = myScanner.nextInt();
		int numCols = myScanner.nextInt();
		
		myScanner.nextLine();
		
		char[][] charArray = new char[((numRows * 2) + 1)][];
		
		String curString;
		int counter = 0;
		while(myScanner.hasNextLine()){
			curString = myScanner.nextLine();
			charArray[counter] = curString.toCharArray();
			counter++;
		}
		
		counter = 0;
		
		//print maze
		/*
		for(int r = 0; r < charArray.length; r++){
			for(int c = 0; c < charArray[r].length; c++){
				System.out.print(charArray[r][c]);
			}
			System.out.println();
		}
		*/
		node worked;
		
		if((worked = bfs(charArray)) != null){
			System.out.print("BFS: " + worked.steps + " cost = " + worked.totalCost +"\n");
		}
		else{
			System.out.println("BFS failed to return a solution.");
		}
		
		if((worked = dfs(charArray)) != null){
			System.out.print("DFS: " + worked.steps + " cost = " + worked.totalCost +"\n");
		}
		else{
			System.out.println("DFS failed to return a solution.");
		}
		
		if((worked = ucs(charArray)) != null){
			System.out.print("UCS: " + worked.steps + " cost = " + worked.totalCost +"\n");
		}
		else{
			System.out.println("UCS failed to return a solution.");
		}
		
		
		System.out.print("\n");
	}
	
	public class changeComparatorUCS implements Comparator<node>{
		public int compare(node n1, node n2){
			
			return n1.totalCost - n2.totalCost;
		}
	}
}
