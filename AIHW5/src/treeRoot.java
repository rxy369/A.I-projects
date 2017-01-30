import java.util.LinkedList;


public class treeRoot {

	static double numPosResults = 0;
	static double numNegResults = 0;
	/*LinkedList posResults = new LinkedList();
	 *LinkedList negResults = new LinkedList();
	*/
	
	static int GetAttribute(boolean[][] data){
		int lastCol = data[0].length;
		numPosResults = 0;
		numNegResults = 0;
		setResults(data);
		
		
		double highestIVal = -1;
		int bestCol = 0;
		//do I calc
		
		for(int j = 0; j < data[0].length - 1; j++){
			double numPosTrue = 0; //  values that are true and have a positive class
			double numPosFalse = 0; //  values that are false and have a positive class
			double numNegTrue = 0; //  values that are true and have a neg class
			double numNegFalse = 0; //  values that are false and a neg class
			double totalTrue = 0; // total amount of true values
			double totalFalse = 0; // total amount false values
			double curIVal = 0;  
			//print("Col: " + j);
			for(int i = 0; i < data.length; i++){
				//System.out.print("" + data[i][j]);
				//determine if the value is true 
				if(data[i][j] == true){
					//and it also leads to a positive value
					if(data[i][lastCol-1] == true){
						numPosTrue++;
					}
					else{
						numNegTrue++;
					}
				}
				else{
					if(data[i][lastCol-1] == true){
						numPosFalse++;
					}
					else{
						numNegFalse++;
					}
				}
			}
			//System.out.println("");
			
			totalTrue = numPosTrue + numNegTrue;
			totalFalse = numPosFalse + numNegFalse;
			//totalVals = totalTrue + totalFalse;
			
			//calc the I vals
			//print("Col "+ j + " numPosResults " + numPosResults + " numNegResults " + numNegResults + " numPosTrue " + numPosTrue + " numNegTrue " + numNegTrue + " numPosFalse " + numPosFalse + " numNegFalse " + numNegFalse);
			double leftSide = doICalc(numPosTrue,numNegTrue,totalTrue);
			double rightSide = doICalc(numPosFalse,numNegFalse,totalFalse);
			//int totalResults = numPosResults +numNegResults;
			double totalResults = totalTrue +totalFalse;
			
			curIVal = doICalc(numPosResults, numNegResults, totalResults) - (((totalTrue/totalResults) * leftSide) + ((totalFalse/totalResults) * rightSide));
			//print("Col "+ j + " firstval " + doICalc(numPosResults, numNegResults, totalResults) + " left side " + (doICalc(numPosTrue,numNegTrue,totalTrue) + " right side " + doICalc(numPosFalse,numNegFalse,totalFalse)));
			//print("Col " + j + " curIVal " + curIVal);
			//print(""+curIVal);
			if(curIVal > highestIVal){
				
				highestIVal = curIVal;
				bestCol = j;
				//print("new highest value: " + curIVal + " best Col: " + bestCol);
			}
			
		}
		//System.out.println(highestIVal);
		return bestCol;
	}
	
	static void setResults(boolean[][] data){
		int lastCol = data[0].length;
		for(int i = 0; i < data.length; i++){
			if(data[i][lastCol-1] == true){
				numPosResults++;
			}
			else{
				numNegResults++;
			}
		}
	}
	
	//returns the I value depending on the pos, neg, and total num values
	static double doICalc(double posVals, double negVals, double totalVals ){
		
		double leftVal = posVals/totalVals;
		double rightVal = negVals/totalVals;
		//print("left val: " + leftVal);
		//print("right val: " + rightVal);
		
		if(leftVal == 1 && rightVal == 0 || leftVal == 0 && rightVal == 1 ){
			return 0;
		}
		else if((leftVal == .1 && rightVal == .9 ) || (leftVal == .9 && rightVal == .1) ){
			return .47;	
		}
		else if((leftVal == .2 && rightVal == .8) || (leftVal == .8 && rightVal == .2 )){
			return .72;
		}
		else if((leftVal == .25 && rightVal == .75) || (leftVal == .75 && rightVal == .25 )){
			return .81;
		}
		else if((leftVal == (1.0/3.0) && rightVal == (2.0/3.0)) || (leftVal == (2.0/3.0) && rightVal == (1.0/3.0) )){
			return .92;
		}
		else if((leftVal == .375 && rightVal == .625) || (leftVal == .625 && rightVal == .375 )){
			return .95;
		}
		else if((leftVal == .4 && rightVal == .6 )|| (leftVal == .6 && rightVal == .4) ){
			return .97;
		}
		else if((leftVal == (3.0/7.0) && rightVal == (4.0/7.0) )|| (leftVal == (4.0/7.0) && rightVal == (3.0/7.0) )){
			return .98;
		}
		else if((leftVal == (4.0/9.0) && rightVal == (5.0/9.0) )|| (leftVal == (5.0/9.0) && rightVal == (4.0/9.0) )){
			return .99;
		}
		else if(leftVal == .5 && rightVal == .5){
			return 1;
		}
		else
			return -1;
	}
	
	public static void print(String str){
		System.out.println(str);
	}

	public static void main(String[] args)
	{
		boolean [][] data1 = {
			{  true,  true,  true,  true,  true},
			{  true,  true, false,  true,  true},
			{  true,  true,  true, false,  true},
			{  true, false,  true,  true,  true},
			{ false,  true, false, false, false},
			{ false,  true,  true, false, false},
			{ false,  true, false,  true, false},
			{ false,  true, false, false, false},
		};
		boolean [][] data2 = {
			{ false, false,  true,  true,  true,  true},
			{ false, false,  true,  true, false,  true},
			{  true, false,  true, false,  true,  true},
			{  true,  true,  true, false, false,  true},
			{ false, false,  true,  true, false, false},
			{  true, false,  true,  true,  true, false},
			{  true, false, false, false,  true, false},
			{ false, false, false, false,  true, false},
		};
		System.out.println(GetAttribute(data1));		// should output 0
		System.out.println(GetAttribute(data2)); 		// should output 2
	}
}
