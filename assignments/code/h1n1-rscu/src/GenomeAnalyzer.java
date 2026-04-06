import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GenomeAnalyzer
{
    ArrayList<CodonEntry> codonList = new ArrayList<>();

    public void loadAminoAcids(String aaCsvPath) throws IOException {
        codonList.clear();
        try (Scanner infile = new Scanner(new File(aaCsvPath))) {
            infile.nextLine(); // throw away header line
            while (infile.hasNext()) {
                String line = infile.nextLine().trim();
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String codon = parts[0].trim().toUpperCase();
                String aaName = parts[1].trim();
                char aaCode = parts[2].trim().charAt(0);
                codonList.add(new CodonEntry(codon, aaName, aaCode));
            }
        }
    }

    public void parseFastaToCounts(String fastaPath, boolean isFileA) throws IOException {
        StringBuilder seqBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fastaPath))) {
            // Read and discard the first line (FASTA header)
            br.readLine(); // discard header line
            // Now read the remaining lines (sequence lines) and append
            String line;
            while ((line = br.readLine()) != null) {
                seqBuilder.append(line.trim());
            }
        }

        // Convert the accumulated sequence builder into an uppercase string for further processing
        String seq = seqBuilder.toString().toUpperCase();

        // GC content calculation removed

        // Process codons in blocks of 3
        int invalidCounter = 0;
        for (int i = 0; i + 3 <= seq.length(); i += 3) {
            String codon = seq.substring(i, i + 3);
            // skip codons containing ambiguous base 'N'
            if (codon.indexOf('N') >= 0) {
                invalidCounter++;
                continue;
            }
            // find matching CodonEntry
            boolean found = false;
            for (CodonEntry ce : codonList) {
                if (ce.getCodon().equals(codon)) {
                    if (isFileA) ce.incrementCountA();
                    else ce.incrementCountB();
                    found = true;
                    break;
                }
            }
            if (!found) {
                // If codon not in list, treat as invalid (shouldn't happen if list has 64)
                invalidCounter++;
            }
        }
        // report skipped/invalid codons
        if (invalidCounter > 0) {
            System.out.printf("Skipped %d invalid/unknown codons while parsing %s\n", invalidCounter, fastaPath);
        }
    }

    public void computeRSCU() {
        // For each codon, compute sum of counts for its aaName and number of synonyms
        for (CodonEntry ce : codonList) {
            String aa = ce.getAaName();
            int sumA = 0;
            int sumB = 0;
            int synonyms = 0;
            for (CodonEntry other : codonList) {
                if (other.getAaName().equals(aa)) {
                    sumA += other.getCountA();
                    sumB += other.getCountB();
                    synonyms++;
                }
            }
            double expectedA = 0.0;
            double expectedB = 0.0;
            if (synonyms > 0) {
                expectedA = ((double) sumA) / synonyms;
                expectedB = ((double) sumB) / synonyms;
            }
            if (expectedA > 0.0) ce.setRscuA(((double) ce.getCountA()) / expectedA);
            else ce.setRscuA(0.0);
            if (expectedB > 0.0) ce.setRscuB(((double) ce.getCountB()) / expectedB);
            else ce.setRscuB(0.0);
        }
    }

//    public void sortCodons() {
//        Collections.sort(codonList, new Comparator<CodonEntry>() {
//            public int compare(CodonEntry a, CodonEntry b) {
//                int cmp = a.aaName.compareToIgnoreCase(b.aaName);
//                if (cmp != 0) return cmp;
//                return a.sequence.compareTo(b.sequence);
//            }
//        });
//    }

    public void generateReport(String outCsvPath) throws IOException {
        // Executive Summary
        System.out.println("Executive Summary:");

        // Table header
        System.out.printf("%-15s %-7s %10s %10s %12s\n", "AA", "Codon", "RSCU_A", "RSCU_B", "% Change");
        System.out.println("------------------------------------------------------------------");

        BufferedWriter bw = new BufferedWriter(new FileWriter(outCsvPath));
        bw.write("AA,Codon,RSCU_A,RSCU_B,PercentChange\n");

        for (CodonEntry ce : codonList) {
            String aa = ce.getAaName();
            String codon = ce.getCodon();
            double rA = ce.getRscuA();
            double rB = ce.getRscuB();
            String pctChangeStr = "N/A";
            if (rA != 0.0) {
                double pct = ((rB - rA) / rA) * 100.0;
                pctChangeStr = String.format("%.2f", pct);
            }
            System.out.printf("%-15s %-7s %10.3f %10.3f %12s\n", aa, codon, rA, rB, pctChangeStr);
            bw.write(String.format("%s,%s,%.6f,%.6f,%s\n", aa, codon, rA, rB, pctChangeStr));

            // ZAP flagging for Arginine group - detect CpG depletion
            if (aa.equalsIgnoreCase("Arginine")) {
                // Assumption: "significantly lower" means more than 50% decrease (rscuB < 0.5 * rscuA)
                if (rA > 0.0 && rB < 0.5 * rA) {
                    System.out.printf("*** ZAP FLAG: Arginine codon %s shows potential CpG depletion (RSCU A=%.3f, RSCU B=%.3f)\n", codon, rA, rB);
                }
            }
        }

        bw.close();
        System.out.println("\nResults exported to: " + outCsvPath);
    }

    /**
     * Generate a per-file codon report matching the sample CSV format:
     * Codon,AA_Name,AA_Code,Count,Total_AA_Count,Num_Synonyms,Percentage,RSCU
     * If isFileA is true, uses counts/RSCU for file A; otherwise for file B.
     */
    public void generatePerFileReport(String outCsvPath, boolean isFileA) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outCsvPath));
        // New header: remove Num_Synonyms; Percentage will include a % sign
        bw.write("Codon,AA_Name,AA_Code,Count,Total_AA_Count,Percentage,RSCU\n");

        for (CodonEntry ce : codonList) {
            String codon = ce.getCodon();
            String aaName = ce.getAaName();
            char aaCode = ce.getAaCode();
            int count = isFileA ? ce.getCountA() : ce.getCountB();

            // compute total count for this amino acid
            int totalAA = 0;
            for (CodonEntry other : codonList) {
                if (other.getAaName().equals(aaName)) {
                    totalAA += isFileA ? other.getCountA() : other.getCountB();
                }
            }

            double percentage = totalAA > 0 ? ((double) count / (double) totalAA) * 100.0 : 0.0;
            double rscu = isFileA ? ce.getRscuA() : ce.getRscuB();

            // Count and Total_AA_Count as integers; Percentage with 2 decimals and a % sign; RSCU with 2 decimals
            bw.write(String.format("%s,%s,%c,%d,%d,%.2f%%,%.2f\n",
                    codon, aaName, aaCode, count, totalAA, percentage, rscu));
        }

        bw.close();
        System.out.println("Per-file report exported to: " + outCsvPath);
    }

    /**
     * Generate the final analysis report combining replicase (A) and spike (B) metrics.
     * Header: Codon,AA_Name,AA_Code,Pct_Replicase,Pct_Spike,Pct_Diff,RSCU_Replicase,RSCU_Spike,RSCU_Diff
     * Pct_Diff and RSCU_Diff are computed as (Spike - Replicase).
     */
    public void generateFinalAnalysisReport(String outCsvPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outCsvPath));
        bw.write("Codon,AA_Name,AA_Code,Pct_Replicase,Pct_Spike,Pct_Diff,RSCU_Replicase,RSCU_Spike,RSCU_Diff\n");

        for (CodonEntry ce : codonList) {
            String codon = ce.getCodon();
            String aaName = ce.getAaName();
            char aaCode = ce.getAaCode();

            // compute totals per amino acid for replicase (A) and spike (B)
            int totalA = 0;
            int totalB = 0;
            for (CodonEntry other : codonList) {
                if (other.getAaName().equals(aaName)) {
                    totalA += other.getCountA();
                    totalB += other.getCountB();
                }
            }

            double pctA = totalA > 0 ? ((double) ce.getCountA() / (double) totalA) * 100.0 : 0.0;
            double pctB = totalB > 0 ? ((double) ce.getCountB() / (double) totalB) * 100.0 : 0.0;
            double rA = ce.getRscuA();
            double rB = ce.getRscuB();

            double pctDiff = pctB - pctA; // Spike - Replicase
            double rscuDiff = rB - rA;

            bw.write(String.format("%s,%s,%c,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f\n",
                    codon, aaName, aaCode, pctA, pctB, pctDiff, rA, rB, rscuDiff));
        }

        bw.close();
        System.out.println("Final analysis exported to: " + outCsvPath);
    }


}
