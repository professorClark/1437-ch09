import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class LegoAnalysis
{
    public static void main(String[] args) throws FileNotFoundException {
        //read in the legosets.csv file. It's big.
        ArrayList<LegoSet> allSets = new ArrayList<>(27000);
        //I scrubbed the data file for you - some data wasn't formatted well and the .split would have problems.
        //you're welcome :)
        Scanner infile = new Scanner(new File("legosets_cleaned.csv"));
        Scanner kbd = new Scanner(System.in); //yes, two Scanners. One for the file, and one for the keyboard!
        infile.nextLine(); // skip the header
        while (infile.hasNextLine()) {
            String line = infile.nextLine();
            String[] tokens = line.split(",");
            String name = tokens[1]; //name is second column
            int year = Integer.parseInt(tokens[2]); //third column. let's get this as an int
            int numParts = Integer.parseInt(tokens[4]); //we want num tokens as an int too
            allSets.add(new LegoSet(name, year, numParts)); //add that new record
        }
        infile.close();
        //now we have all the sets in an ArrayList. Let's do some analysis.
        //How many sets are there?
        System.out.println("There are " + allSets.size() + " Lego allSets.");
        //I wonder how many years of sets are in the list. Newest - oldest would tell us that.
        int newest = allSets.get(0).year(); //don't worry about the yellow underline. I can talk about that in class.
        int oldest = allSets.get(0).year();
        for (LegoSet set : allSets) {
            if (set.year() > newest)
                newest = set.year();
            if (set.year() < oldest)
                oldest = set.year();
        }
        System.out.println("The oldest set is from " + oldest + " and the newest is from " + newest + ".");
        System.out.println("We have " + (newest - oldest) + " years of lego sets. Cool.");
        //What else would be cool to know? I wonder how many sets have more than 2000 pieces.
        //I'd build those, but probably couldn't afford them all :)
        //I did this and ran it - turns out there are 293 sets with 2000+ pieces!
        //I'm going to ask the user for the highest number of parts instead.
        int count = 0, setSize;
        System.out.print("What's the highest number of pieces you want to find: ");
        setSize = kbd.nextInt();
        for (LegoSet set : allSets) {
            if (set.numParts() >= setSize)
                count++;
        }
        System.out.println("There are " + count + " Lego sets with " + setSize + " or more pieces.");

        //Let's let them print out those sets if they want. I'm going to sort them by number of pieces
        //since the .csv isn't in any real order! (what?)
        //don't worry about this code so much. It's using a Comparator.
        //And yes, this is inefficient since I'm rerunning a loop. I'll write cleaner code in another file for you
        System.out.println("Do you want me to list all of those sets? y/n: ");
        char choice = kbd.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            ArrayList<LegoSet> sortedSets = new ArrayList<>(count); //to hold just the sets we want. We know exactly how many
            //build the ArrayList of only the sets with the right number of pieces
            for (LegoSet current : allSets) {
                if(current.numParts() >= setSize) {
                    sortedSets.add(current); //this one has the right number of pieces. Add it to our smaller arrayList
                }
            }
            //this is the Comparator sorting code I mentioned in the previous comment
            sortedSets.sort(Comparator.comparingInt(LegoSet::numParts).reversed());
            //now they're sorted by the number of parts (largest first). Let's print those out.
            System.out.println("Here are the sets with " + setSize + " or more pieces:");
            for (LegoSet set : sortedSets) {
                System.out.println(set); //calling our own toString
            }
        }



    }
}