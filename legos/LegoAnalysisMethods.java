import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * This is the LegoAnalysis class rewritten using methods. Much cleaner IMO.
 */

public class LegoAnalysisMethods
{
    public static void main(String[] args) throws IOException //## read note below
    {
        ArrayList<LegoSet> allSets = buildList(); //the build method returns our complete list
        //## main needs a throws IOException too since buildList() could throw it to main. More in Chapter 11.
        Scanner kbd = new Scanner(System.in); //so we can ask the user for sets to search

        //How many sets are there?
        System.out.println("There are " + allSets.size() + " Lego sets.");
        //What's the difference in highest vs lowest dates of the sets?
        yearDifference(allSets); //will print the info about the year differential

        //Let's ask for the minimum number of parts they want to find.
        System.out.print("What's the minimum number of pieces you want to find in a set: ");
        int minSize = kbd.nextInt();
        int count = findLargeSets(allSets, minSize);
        System.out.println("There are " + count + " Lego sets with " + minSize + " or more pieces.");

        //Let's let them print out those sets if they want. I'm going to sort them by number of pieces
        System.out.println("Do you want me to list all of those sets? y/n: ");
        char choice = kbd.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            //returns a smaller arraylist with just the sets with the right number of pieces, sorted by number of pieces (largest first)
            ArrayList<LegoSet> sortedSets = sortList(allSets,count,minSize);
            //now they're sorted by the number of parts (largest first). Let's print those out.
            System.out.println("Here are the sets with " + minSize + " or more pieces:");
            for (LegoSet set : sortedSets) {
                System.out.println(set); //calling our own toString
            }
        }
    }

    /**
     * This method opens the file and creates the arraylist of all lego sets.
     * Note that THIS is the method has a throws IOException
     * since this is the method is trying to work with the file.
     * @return an ArrayList full of all Lego sets from our file.
     * @throws IOException
     */
    public static ArrayList<LegoSet> buildList() throws IOException
    {
        ArrayList<LegoSet> temp = new ArrayList<>(27000);
        try (Scanner infile = new Scanner(Path.of("legosets_cleaned.csv")))
        {
            String[] tokens;
            infile.nextLine(); // skip the header
            while(infile.hasNextLine())

            {
                String line = infile.nextLine();
                tokens = line.split(",");
                String name = tokens[1]; //name is second column
                int year = Integer.parseInt(tokens[2]); //third column. let's get this as an int
                int numParts = Integer.parseInt(tokens[4]); //we want num tokens as an int too
                temp.add(new LegoSet(name, year, numParts)); //add that new record
            }
        }
        //we've got all the sets now. return that ArrayList
        return temp;
    }

    /**
     * This method determines the oldest and newest years of Lego sets
     * and displays info about the year differential
     * @param set - the ArrayList to use for year analysis
     */
    public static void yearDifference(ArrayList<LegoSet> set)
    {
        int newest = set.getFirst().year(); //I didn't cover this, but you can getFirst instead of get(0)
        int oldest = set.getFirst().year();
        for (LegoSet s : set) {
            if (s.year() > newest)
                newest = s.year();
            if (s.year() < oldest)
                oldest = s.year();
        }
        System.out.println("The oldest set is from " + oldest + " and the newest is from " + newest + ".");
        System.out.println("We have " + (newest - oldest) + " years of lego sets. Cool.");
    }

    /**
     *
     * @param sets - the ArrayList to search
     * @param minSetSize - the min number of pieces in a set
     * @return how many sets have the required number of pieces
     */
    public static int findLargeSets(ArrayList<LegoSet> sets, int minSetSize)
    {   int count = 0;
        for (LegoSet current : sets) {
            if (current.numParts() >= minSetSize)
                count++;
        }
        return count;
    }

    /**
     * This method sorts an ArrayList by the number of parts
     * @param sets the entire ArrayList of Lego sets
     * @param numSets how many sets there are that match our criteria
     * @param minParts the minimum number of parts in a set
     * @return a sorted ArrayList of sets with the minimum number of pieces, sorted by largest number of pieces first
     */
    public static ArrayList<LegoSet> sortList(ArrayList<LegoSet> sets, int numSets, int minParts)
    {
        ArrayList <LegoSet> temp = new ArrayList<>(numSets); //to hold just the sets we want. We know exactly how many
        //build the ArrayList of only the sets with the right number of pieces
        for (LegoSet current : sets) {
            if(current.numParts() >=  minParts ) {
                temp.add(current); //this one has the right number of pieces. Add it to our smaller arrayList
            }
        }
        //now sort that set with a Comparator.
        temp.sort(Comparator.comparingInt(LegoSet::numParts).reversed());
        //we can return the sorted list now.
        return temp;
    }
}