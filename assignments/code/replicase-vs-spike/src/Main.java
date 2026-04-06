import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {

            String aaCsv = "amino-acids.csv";
            String fileA = "replicase.fasta";
            String fileB = "spike.fasta";
            if (args.length >= 2) {
                fileA = args[0];
                fileB = args[1];
            }
            if (args.length >= 3) aaCsv = args[2];

            GenomeAnalyzer ga = new GenomeAnalyzer();
            ga.loadAminoAcids(aaCsv);
            ga.parseFastaToCounts(fileA, true);
            ga.parseFastaToCounts(fileB, false);
            ga.computeRSCU();
            ga.sortCodons();
            String out = "Analysis_Results.csv";
            ga.generateReport(out);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

