import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    private static String[] tokens;
    private static ArrayList<AminoAcid> acidTable;

    public static void main(String[] args) throws FileNotFoundException {
        acidTable = getAcids();
        String filename = "covidSequence19_S25_Clean.csv";
        File f = new File(filename);
        Scanner infile = new Scanner(f);
        String str = infile.nextLine();
        tokens = str.split(",");
        infile.close();
        createAAReport();
    }

    public static ArrayList<AminoAcid> getAcids() throws FileNotFoundException {
        File f = new File("aminoAcidTable.csv");
        Scanner infile = new Scanner(f);
        ArrayList<AminoAcid> a = new ArrayList<>();
        //throw away header line
        infile.nextLine();
        //split each line into a new amino acid
        while (infile.hasNext()) {
            String str = infile.nextLine();
            String[] tokens = str.split(",");
            ArrayList<String> options = new ArrayList<>();
            for (int i = 3; i < tokens.length; i++)
                options.add(tokens[i]);
            a.add(new AminoAcid(tokens[0], tokens[2], options.toArray(new String[0])));
        }
        infile.close();
        return a;
    }

    public static void createAAReport() throws FileNotFoundException {
        String[] values = {};
        PrintWriter outfile = new PrintWriter("C19_S25_CodonBias.txt");
        outfile.println("***** Measles Codon Bias Analysis *****\n");
        //find the codon and print the name and codon options
        for (AminoAcid a : acidTable)
        {
            outfile.println(a);

            values = a.getCodons();
            int[] counters = new int[values.length];
            //loop through all the tokens in the genome file looking for matches to the codons in values
            for (String s : tokens) {
                for (int i = 0; i < values.length; i++) {
                    if (s.equals(values[i]))
                        counters[i]++;
                }
            }

            //sum total number of the amino acid found in the genome
            int sumAminoAcid = 0;
            for (int i = 0; i < values.length; i++) {
                sumAminoAcid += counters[i];
            }

            //print the percentages of each codon in the amino acid
            for (int i = 0; i < values.length; i++) {
                double percent = (double) counters[i] / sumAminoAcid * 100;
                String output = values[i] + ": " + String.format("%3d%8.2f", counters[i], percent) + "%";
                outfile.println(output);
            }
            //print the percentage of the genome for that amino acid
            double percent = (double) sumAminoAcid/ tokens.length * 100;
            outfile.printf("Overall Genome Encoding: %6.2f%%%n", percent);
            outfile.println();
        }
        outfile.close();
    }

}