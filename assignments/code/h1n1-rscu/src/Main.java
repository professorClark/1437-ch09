import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {

            String aaCsv = "codons.csv";
            String fileA = "h1n1-pb1.fasta";
            String fileB = "h1n1-ha.fasta";
            GenomeAnalyzer ga = new GenomeAnalyzer();
            ga.loadAminoAcids(aaCsv);
            ga.parseFastaToCounts(fileA, true);
            ga.parseFastaToCounts(fileB, false);
            ga.computeRSCU();
            String out = "h1n1-analysis-results.csv";
            //ga.generateReport(out); //executive report to screen
            // Also generate per-file codon reports matching the sample CSV format
            ga.generatePerFileReport("h1n1_pb1_replicase_codon_data.csv", true);
            ga.generatePerFileReport("h1n1_ha_codon_data.csv", false);
            // Generate final combined analysis report
            ga.generateFinalAnalysisReport("h1n1_analysis_results.csv");
            // Print executive-ready UP/DOWN category shift report to console and file
            ga.printCategoryChangeReport("h1n1_summary_report.txt");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

