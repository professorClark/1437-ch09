public class CodonEntry {
    private String codon;    // 3-letter codon
    private String aaName;      // Amino Acid full name
    private char aaCode;        // 1-letter code
    private int countA;         // frequency in file A (Replicase)
    private int countB;         // frequency in file B (Spike)
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

    public void setCountA(int countA) {
        this.countA = countA;
    }

    public void setCountB(int countB) {
        this.countB = countB;
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

