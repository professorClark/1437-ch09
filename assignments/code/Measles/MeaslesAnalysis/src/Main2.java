//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Scanner;
//
////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main2 {
//    public static void main(String[] args) throws FileNotFoundException {
//        File f = new File("cleanMeaslesSequenceTestData.txt");
//        Scanner infile = new Scanner(f);
//        //String str = infile.nextLine();
//
//        File f2 = new File("aminoAcidTable.csv");
//        Scanner infile2 = new Scanner(f2);
//        ArrayList<String> codons = new ArrayList<>();
//        ArrayList<String> aminoAcid = new ArrayList<>();
//        //throw away header line
//        infile2.nextLine();
//        //split each line into its two parts of the arrayList
//        while (infile2.hasNext()) {
//            String str = infile2.nextLine();
//            String[] tokens = str.split(",");
//            //find each codon possibility
//            for (int i = 3; i < tokens.length; i++) {
//                codons.add(tokens[i]);
//                aminoAcid.add(tokens[2]); //[2] is where the letter is
//            }
//        }
//        infile2.close();
//        //now we have two arraylists, one for codons and one for corresponding amino acids.
//        //check each codon in the fasta file and print out the corresponding AA code
//        String[] allCodons = infile.nextLine().split(",");
////        for (String c : allCodons)
////        {
////            //find codon
////            String aaLetter = aminoAcid.get(codons.indexOf(c));
////            System.out.println(aaLetter);
////        }
//        //we need to build the genes. Check to see if the codon is a start codon. If so,
//        //start building the string.
//        for (String c : allCodons)
//        {
//            if (c.equals("ATG"))
//            {
//                StringBuilder gene = new StringBuilder();
//                //add the M amino acid and then keep going until we hit a stop
//                do {
//                        gene.append(aminoAcid.get(codons.indexOf(c)));
//                    } while
//                }
//            }
//        }
//
//
//        infile.close();
//
//        //System.out.println(acids);
////            ArrayList<Gene> genes = new ArrayList<Gene>();
////            String[] tokens = str.split(",");
////            //create an arraylist of amino acids
////
////            String s = "Hi ";
////            int count = 0;
////            for(String codon : tokens)
////            {
////                //if codon = ATG, start building
////                StringBuilder geneStr = new StringBuilder();
////                do {
////
////                    geneStr.append(s);
////                    count++;
////                } while (count < 3);
////                //make gene
////                genes.add(new Gene(geneStr.toString()));
////            }
////
////            //print out each gene with a letter determiner
////            char geneNum = 'A';
////            for (Gene g : genes)
////            {
////                System.out.println("Gene " + geneNum + ": " + g);
////                geneNum++;
////            }
//
//    }
//}