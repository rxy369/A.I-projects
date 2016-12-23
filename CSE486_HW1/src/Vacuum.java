
public class Vacuum {

	//I had notes here before but after talking about it with some friends
	//I realized that I was approaching the question wrong entirely. To put it 
	//bluntly, I was planning on using doubly linked lists so it was way too complicated of a strategy.
	//Plus I just interpreted some of the rules wrong so I glad I talked it over with them.
	
	int curSpace;
	boolean[] spaces; //if a boolean is false, then it is clean, otherwise it is dirty
	int addValue; // either increases or decreases the current space by 1. Is either 1 or -1
	int numPoints;
	
	public Vacuum(){
		curSpace = 0;
		spaces = new boolean[4];
		addValue = 1;
		numPoints = 0;
	}
	
	// everything that happens in one step in a specific order
	private void step(double SChance,double PChance){
		dirtySpaces();
		reverseDirection();
		checkDirty(SChance,PChance);
		addPoints();
	}
	
	// checks array boolean, if false, then have a 20% to make it dirty
	private void dirtySpaces(){
		
		for(int o = 0; o < 4; o++){
			if(spaces[o] == false){
				if(Math.random() < .20){
					spaces[o] = true;
				}	
			}
		}
	}
	
	//checks if the vacuum incorrectly interperets the space its on correctly
	//then based on the curSpace either sucks or moves
	private void checkDirty(double SChance, double PChance){
		//boolean tempBool = spaces[curSpace];
		if(PChance < 1){
			if(Math.random() > PChance){ //checks to see if it fails, PChance is the chance for it to succeed
				//System.out.println(curSpace);
				if(spaces[curSpace] == false){ // thinks its dirty but its clean 
					//System.out.println("thinks its dirty, sucks");
					suck(SChance);
				}
				else{// thinks its clean,but its dirty
					//System.out.println("thinks its clean but its dirty");
					move();
				}
			}
			
			else if(Math.random() < PChance){ //checks to see if it succeeds, PChance is the chance for it to succeed
				if(spaces[curSpace] == true){// if the space is dirty, clean it
					//System.out.println("correct in that its dirty");
					suck(SChance);
				}
				else { // if its clean, move
					//System.out.println("correct in that it is clean");
					move();
				}
					
			}
		}
	}
	
	//attempts the suck in the space the vacuum cleaner is in given the chance value.
	//If it fails, the spot doesn't change. Otherwise makes it clean.
	private void suck(double SChance){
		if(Math.random() < SChance){
			//System.out.println("sucked successfully");
			if(spaces[curSpace] == true){
				spaces[curSpace] = false;
			}
		}
	}
	
	//reverse the direction of the vacuum
	// Shoutout to Stephen Weisenberger for this solution
	private void reverseDirection(){
		if(curSpace == 0){
			//System.err.println("reverse Direction to 1");
			addValue = 1; 
		}
		if(curSpace == 3){
			//System.err.println("reverse Direction to -1");
			addValue = -1; 
		}
	}
	
	// moves the vacuum in a specific direction based on addValue 
	private void move(){
		//System.out.println(curSpace);
		curSpace += addValue;
	}
	
	//adds a point for every clean spacein the array
	private void addPoints(){
		for(int i = 0; i < 4; i++){
			if(spaces[i] == false){
				numPoints++;
			}
		}
	}
	
	public int getPoints(){
		return numPoints;
	}
	
	// will handle the amount of steps and writing to the console 
	public static void main(String[] args) {
		Vacuum test0 = new Vacuum(); //(0%,10%)
		Vacuum test1 = new Vacuum(); //(50%,10%)
		Vacuum test2 = new Vacuum(); //(100%,10%)
		Vacuum test3 = new Vacuum(); //(0%,90%)
		Vacuum test4 = new Vacuum(); //(50%,90%)
		Vacuum test5 = new Vacuum(); //(100%,90%)
		
		for(int i = 0; i < 2000; i++){
			test0.step(0,.1);
			test1.step(.25,.1);
			test2.step(1,.1);
			test3.step(0,.9);
			test4.step(.25,.9);
			test5.step(1,.9);
		}
		//prints out the point values for each test
		System.out.println("Points (S:0%,P:10%): " + test0.getPoints());
		System.out.println("Points (S:25%,P:10%): " + test1.getPoints());
		System.out.println("Points (S:100%,P:10%): " + test2.getPoints());
		System.out.println("Points (S:0%,P:10%): " + test3.getPoints());
		System.out.println("Points (S:25%,P:10%): " + test4.getPoints());
		System.out.println("Points (S:100%,P:10%): " + test5.getPoints());
		
	}
	
}
	







