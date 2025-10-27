import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static String[] tokens;
    private static ArrayList<AminoAcid> acidTable;
    private static ArrayList<Gene> genes;
    final static int ORF = 50; //minimum length 50 AAs for a gene

    public static void main(String[] args) throws FileNotFoundException {
        Scanner kbd = new Scanner(System.in);
        acidTable = getAcids("aminoAcidTable.csv");
        System.out.println("******Measles Genome Analysis******");
        System.out.print("Choose a Reading Frame (1,2,3): ");
        int readingFrame = kbd.nextInt();

        String filename;
        if (readingFrame == 1)
            filename = "measlesSequenceRF1.csv";
        else if (readingFrame == 2)
            filename = "measlesSequenceRF2.csv";
        else
            filename = "measlesSequenceRF3.csv";
        File f = new File(filename);
        Scanner infile = new Scanner(f);
        String str = infile.nextLine();
        tokens = str.split(",");
        infile.close();
        codonBias(filename); //run codon bias analysis
        geneAnalysis(filename);
    }


    public static ArrayList<AminoAcid> getAcids(String filename) throws FileNotFoundException {
        File f = new File(filename);
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
            a.add(new AminoAcid(tokens[0], tokens[1], tokens[2], options.toArray(new String[0])));
        }
        infile.close();
        return a;
    }

    public static void codonBias(String filename) throws FileNotFoundException {
        Scanner kbd = new Scanner((System.in));
        String newFileName = filename.substring(0,filename.length()-4) + "_CodonBias.txt";
        PrintWriter outfile = new PrintWriter(newFileName);
        printMenu();
        int option = kbd.nextInt();
        if (option == 1) {
            //displayAll()
            //display the report for every AA in our list
            outfile.println(("*********** Complete Codon Bias Report **********\n"));
            for (AminoAcid a : acidTable)
            {
                createAAReport(a.getOneLetter(),outfile);
            }
            outfile.close();
            System.out.println("Codon Bias Report written to " + newFileName);
            //then we are done. No menu.
            return;
        }
        while (option == 2) {
            //chooseAminoAcid();
            System.out.print("Enter an Amino Acid to analyze: ");
            kbd.nextLine(); //skip buffer issue
            String choice = kbd.nextLine();
            System.out.println("********** Codon Bias Analysis for " + choice.toUpperCase() + " ***********");
            createAAReport(choice.toUpperCase(),null);
            //display menu again:
            printMenu();
            option = kbd.nextInt();
        }//while
        outfile.close();
    }

    public static void printMenu()
    {
        System.out.println("******Codon Bias Analysis******");
        System.out.println("Execute which of the following options: ");
        System.out.println("1. Codon Bias Report of all Amino Acids in the genome");
        System.out.println("2. Choose a specific Amino Acid");
        System.out.print("3. Exit Codon Bias Analysis\n>> ");
    }

    public static String getAAName(String c) {
        String name = "";
        for (AminoAcid aa : acidTable) {
            if (aa.contains(c)) {
                name = aa.getOneLetter();
                break;
            }
        }
        return name;
    }


    public static void createAAReport(String AAName, PrintWriter outfile) throws FileNotFoundException {

        String[] values = {};
        boolean found = false;
        //find the codon and print the name and codon options
        for (AminoAcid a : acidTable) {
            if (a.getOneLetter().equals(AAName)) {
                AAName = a.getName();
                String c = "The codons for " + AAName + "(" + a.getOneLetter() + ") are: ";
                if (outfile == null)
                    System.out.print(c);
                else
                    outfile.print(c);
                values = a.getCodons();
                if (outfile == null) {
                    for (int i = 0; i < values.length; i++)
                        System.out.print(values[i] + " ");
                    System.out.println();
                }
                else {
                    for (int i = 0; i < values.length; i++)
                        outfile.print(values[i] + " ");
                    outfile.println();
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println(AAName + " is not a valid amino acid.");
            return;
        }

        int[] counters = new int[values.length];
        //loop through tokens looking for matches to the codons in values
        int i;

        for (String s : tokens) {
            for (i = 0; i < values.length; i++) {
                if (s.equals(values[i]))
                    counters[i]++;
            }
        }

        //sum counters
        //output report
        int sumAminoAcid = 0;
        for (i = 0; i < values.length; i++) {
            sumAminoAcid += counters[i];
        }

        if (outfile == null) {
            for (i = 0; i < values.length; i++) {
                double percent = (double) counters[i] / sumAminoAcid * 100;
                String output = values[i] + ": " + String.format("%3d%8.2f", counters[i],percent) + "%";
                System.out.println(output);
            }
            System.out.println();
        }
        else {
            for (i = 0; i < values.length; i++) {
                double percent = (double) counters[i] / sumAminoAcid * 100;
                String output = values[i] + ": " + String.format("%3d%8.2f", counters[i],percent) + "%";
                outfile.println(output);
            }
            outfile.println();
        }
    }

    public static void geneAnalysis(String filename) throws FileNotFoundException {
        String newFileName = filename.substring(0,filename.length()-4) + "_GeneAnalysis.txt";
        PrintWriter outfile = new PrintWriter(newFileName);
        outfile.println("** Gene analysis for file: " + filename + " **");
        genes = new ArrayList<Gene>();
        boolean geneOn = false;
        StringBuilder geneStr = new StringBuilder();
        int nucleotidePosition = 0;  //start with zero offset for reading frame 1
        int geneStart = 0, geneEnd = 0;
        for (String codon : tokens) {
            //find an ATG. When you do, start building a string with the AAcode for
            //that codon. When you reach a stop, add that gene to the genes AL
            if (codon.equals("ATG") && !geneOn) {
                //start recording a gene
                geneStr = new StringBuilder();
                geneOn = true;  //we are coding now
                geneStart = nucleotidePosition;
            }
            if (geneOn) {
                //add this AA to the string
                geneStr.append(getAAName(codon));
            }
            if (geneOn && (codon.equals("TAG") || codon.equals("TGA") || codon.equals("TAA"))) //or some other stop
            {
                //turn off the gene coding
                geneOn = false;
                geneEnd = nucleotidePosition + 3; //the last nucleotide of this codon
                //add our string we built to our gene AL if length > ORF
                if (geneStr.length() > ORF)
                    genes.add(new Gene(geneStr.toString(), geneStart, geneEnd));
            }
            nucleotidePosition += 3; //we
        }
        int geneNum = 1;
        //print out all genes to file
        for (Gene g : genes) {
            outfile.println("Protein " + geneNum + " (" + g.getLength() + ") :\n"
                    + g.getGeneStartPosition() + ".." + g.getGeneEndPosition() + "\n"
                    + "Sequence: " + g.getAASequence() + "\n");
            geneNum++;
        }
        outfile.close();
    }

}