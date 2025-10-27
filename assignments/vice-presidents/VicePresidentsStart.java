/**
 * This program reads in information from a Vice Presidents file
 * into multiple ArrayLists and determines who was the youngest 
 * Vice President
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class VicePresidents {

	public static void main(String[] args) throws IOException {

		String filename = "VicePresidentAges.csv";
		File file = new File(filename);
		Scanner infile = new Scanner(file);

		//Create two ArrayLists: one to hold the names of the VPs
		//and another to ages of the VPs
		
		while (infile.hasNext())
		{
			String line = infile.nextLine();
			//Divide the line into its tokens (There should be 2 tokens per line)
			
			//put the tokens into their correct ArrayList
		}
		
		infile.close();

		//Go through the ages ArrayList looking for the youngest age.  
		//Hold onto the *index* where the youngest age is located in the ArrayList.
		
		
		
		//Print out the name and age for the youngest Vice President using the index
		//you just found from above
				
			
	}

}