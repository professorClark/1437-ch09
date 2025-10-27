/**
 * This program reads in information from a Vice Presidents file
 * into multiple ArrayLists and determines who was the youngest 
 * Vice President
 * 04-05-2020, edited 10-28-2020
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class VPTester 
{

	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-generated method stub
		
		String filename = "VicePresidentAges.csv";
		File file = new File(filename);
		Scanner infile = new Scanner(file);
		
		int nameCounter = 0;
		int ageCounter = 0;
		
		String name = "";
		int age = 0;
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> ages = new ArrayList<Integer>();
		
		while (infile.hasNext())
		{
			String line = infile.nextLine();
			for(nameCounter = 0; !line.substring(nameCounter, nameCounter+1).equals(","); nameCounter++)
			{
				
			}
			
			name = line.substring(0, nameCounter);
			names.add(name);
			
			age = Integer.parseInt(line.substring(nameCounter+1));
			ages.add(age);
			//Divide the line into its tokens (There should be 2 tokens per line)
			//put the tokens into their correct ArrayList
		}
		
		System.out.println(names);
		System.out.println(ages);
		infile.close();

		//Go through the ages ArrayList looking for the youngest age.  
		//Hold onto the *index* where the youngest age is located in the ArrayList.
		
		int youngestVPage = ages.get(0);
		int youngestVPindex = 0;
		for(int i = 0; i < ages.size(); i++)
		{
			if(ages.get(i) < youngestVPage)
			{
				youngestVPage = ages.get(i);
				youngestVPindex = i;
			}
		}
		
		System.out.println("\nThe youngest Vice President that was sworn into their position is..."
				         + "\n" + names.get(youngestVPindex) + " at age " + youngestVPage);
		//Print out the name and age for the youngest Vice President using the index
		//you just found from above
				
			
	}

}