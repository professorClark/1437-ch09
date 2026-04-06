import java.io.*;
import java.util.*;

public class GenomeAnalyzer {
    ArrayList<CodonEntry> codonList = new ArrayList<CodonEntry>();

    int invalidA = 0;
    int invalidB = 0;

    long gcA = 0;
    long totalBasesA = 0;

    long gcB = 0;
    long totalBasesB = 0;

    public void loadAminoAcids(String aaCsvPath) throws IOException {
        codonList.clear();
        Scanner infile = new Scanner(new File(aaCsvPath));
        String line = null;
        infile.nextLine(); //throw away header line
        while (infile.hasNext())
        {
            line = infile.nextLine().trim();
            String[] parts = line.split(",");
            if (parts.length < 3) continue;
            String codon = parts[0].trim().toUpperCase();
            String aaName = parts[1].trim();
            char aaCode = parts[2].trim().charAt(0);
            codonList.add(new CodonEntry(codon, aaName, aaCode));
        }
        infile.close();
    }

    public void parseFastaToCounts(String fastaPath, boolean isFileA) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fastaPath));
        String line;
        StringBuilder seqBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if (line.startsWith(">")) continue; // skip header lines
            seqBuilder.append(line.trim());
        }
        br.close();

        // GC counting: count per character excluding Ns
        long gc = 0;
        long total = 0;
        for (char c : seq.toCharArray()) {
            if (c == 'A' || c == 'C' || c == 'T' || c == 'G') {
                total++;
                if (c == 'G' || c == 'C') gc++;
            }
        }
        if (isFileA) {
            gcA = gc;
            totalBasesA = total;
        } else {
            gcB = gc;
            totalBasesB = total;
        }

        // Process codons in blocks of 3
        int invalidCounter = 0;
        for (int i = 0; i + 3 <= seq.length(); i += 3) {
            String codon = seq.substring(i, i + 3);
            if (codon.indexOf('N') >= 0) {
                invalidCounter++;
                continue;
            }
            // find matching CodonEntry
            boolean found = false;
            for (CodonEntry ce : codonList) {
                if (ce.sequence.equals(codon)) {
                    if (isFileA) ce.countA++;
                    else ce.countB++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                // If codon not in list, treat as invalid (shouldn't happen if list has 64)
                invalidCounter++;
            }
        }

        if (isFileA) invalidA = invalidCounter;
        else invalidB = invalidCounter;
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
        double gcPercA = totalBasesA > 0 ? (100.0 * ((double) gcA / (double) totalBasesA)) : 0.0;
        double gcPercB = totalBasesB > 0 ? (100.0 * ((double) gcB / (double) totalBasesB)) : 0.0;

        System.out.printf("Executive Summary:\n");
        System.out.printf("File A: Total bases (no N) = %d, GC%% = %.2f\n", totalBasesA, gcPercA);
        System.out.printf("File B: Total bases (no N) = %d, GC%% = %.2f\n", totalBasesB, gcPercB);
        System.out.printf("Invalid codons skipped - File A: %d, File B: %d\n\n", invalidA, invalidB);

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
}

