import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {

            String aaCsv = "codons.csv";
            String fileA = "replicase.fasta";
            String fileB = "spike.fasta";
            GenomeAnalyzer ga = new GenomeAnalyzer();
            ga.loadAminoAcids(aaCsv);
            ga.parseFastaToCounts(fileA, true);
            ga.parseFastaToCounts(fileB, false);
            ga.computeRSCU();
            String out = "covid-analysis-results.csv";
            //ga.generateReport(out); //executive report to screen
            // Also generate per-file codon reports matching the sample CSV format
            ga.generatePerFileReport("covid_replicase_codon_data.csv", true);
            ga.generatePerFileReport("covid_spike_codon_data.csv", false);
            // Generate final combined analysis report
            ga.generateFinalAnalysisReport("covid_analysis_results.csv");
            // Print executive-ready UP/DOWN category shift report to console and file
            ga.printCategoryChangeReport("covid_summary_report.txt");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

