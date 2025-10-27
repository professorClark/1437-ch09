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
		// TODO Auto-generated method stub
		String filename = "VicePresidentAges.csv";
		File file = new File(filename);
		Scanner infile = new Scanner(file);

		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> ages = new ArrayList<Integer>();
		
		while (infile.hasNext())
		{
			String line = infile.nextLine();
			//parse the line into its tokens (There should be 2 tokens per line)
			String[] tokens = line.split(",");
			//put the tokens into their correct ArrayList
			names.add(tokens[0]);
			ages.add(Integer.parseInt(tokens[1]));
		}
		
		infile.close();
		//Go through the ages ArrayList looking for the youngest age.  Hold onto the index
		//where the youngest age is located in the ArrayList.
		int lowestAgeIndex = -1;
		int lowestAge = 1000;
		for (int i = 0; i < ages.size(); i++)
		{
			if (ages.get(i) < lowestAge)
			{
				lowestAge = ages.get(i);
				lowestAgeIndex = i;
			}
		}
		
		//Print out the name and age for the youngest Vice President
		System.out.println("The youngest Vice President was " + names.get(lowestAgeIndex) + ".");
		System.out.println("He was " + ages.get(lowestAgeIndex) + " years old.");
			
	}

}
