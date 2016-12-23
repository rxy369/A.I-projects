
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class uncle {
	public static void main(String[] args) throws Exception {
		Family familia = new Family();
	//read in the files for fathers.txt, mothers.txt, and married.txt
	
	//split them via lines, save them all in a string array per text file 
	
	// loop through the array and see if any of the children match the second argument. 
	//If they do, then we save the parent as a string. Then run the loop again and find the parent of the parent you just found.
	//We want to do this until we find the great grand parent. Once we get that all we have to do is find all the lines that have 
	// the great grandparent as a parent and see if any of those children match the 1st parameter.
	
		System.out.println(familia.greatAuntUncle(Family.Person.MALE6,Family.Person.FEMALE7));
		System.out.println(familia.greatAuntUncle(Family.Person.MALE6,Family.Person.MALE8));
		System.out.println(familia.greatAuntUncle(Family.Person.FEMALE7,Family.Person.MALE3));
		System.out.println(familia.greatAuntUncle(Family.Person.FEMALE4,Family.Person.FEMALE7));
		System.out.println(familia.greatAuntUncle(Family.Person.FEMALE4,Family.Person.MALE3));
		
		System.out.println(familia.greatAuntUncle(Family.Person.MALE3,Family.Person.MALE7));
		
		System.out.println(familia.greatAuntUncle(Family.Person.MALE6,Family.Person.FEMALE7));
		System.out.println(familia.greatAuntUncle(Family.Person.FEMALE7,Family.Person.MALE6));
		
		System.out.println(familia.greatAuntUncle(Family.Person.MALE5,Family.Person.FEMALE8));
		System.out.println(familia.greatAuntUncle(Family.Person.MALE5,Family.Person.MALE8));
		System.out.println(familia.greatAuntUncle(Family.Person.MALE8,Family.Person.MALE8));
	}
}