import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CleanFile {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("measlesSequence.fasta");
        Scanner infile = new Scanner(f);
        PrintWriter outfile = new PrintWriter("cleanMeaslesSequence.txt");

        //read in the string, cut it into chunks of 3
        //add a comma and then spit it back out to a file
        //don't add a , to the last codon - check the length of
        //the string first to not go that far.

        String sequence = infile.nextLine();
        infile.close();
        //drop first two char and last one.
        //sequence = sequence.substring(1,sequence.length()-2);
        int i;
        for (i = 0; i < sequence.length()-3; i+=3)
        {
            outfile.print(sequence.substring(i,i+3) + ",");
        }
        outfile.print(sequence.substring(i,i+3));
        outfile.close();
    }

}
