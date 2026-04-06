public class CodonEntry {
    private final String codon;    // 3-letter codon
    private final String aaName;      // Amino Acid full name
    private final char aaCode;        // 1-letter code
    private int countA;         // frequency in file A (PB1 - Replicase)
    private int countB;         // frequency in file B (HA - surface binding)
    private double rscuA;       // RSCU for file A
    private double rscuB;       // RSCU for file B

    public CodonEntry(String codon, String aaName, char aaCode) {
        this.codon = codon;
        this.aaName = aaName;
        this.aaCode = aaCode;
        this.countA = 0;
        this.countB = 0;
        this.rscuA = 0.0;
        this.rscuB = 0.0;
    }

    // Increment helpers to update counts atomically from callers
    public void incrementCountA() {
        this.countA++;
    }

    public void incrementCountB() {
        this.countB++;
    }

    public void setRscuA(double rscuA) {
        this.rscuA = rscuA;
    }

    public void setRscuB(double rscuB) {
        this.rscuB = rscuB;
    }

    @Override
    public String toString() {
        return aaName + "|" + codon + "|A:" + countA + "|B:" + countB + "|rscuA:" + rscuA + "|rscuB:" + rscuB;
    }

    public String getCodon() {
        return codon;
    }

    public String getAaName() {
        return aaName;
    }

    public char getAaCode() {
        return aaCode;
    }

    public int getCountA() {
        return countA;
    }

    public int getCountB() {
        return countB;
    }

    public double getRscuA() {
        return rscuA;
    }

    public double getRscuB() {
        return rscuB;
    }
}
