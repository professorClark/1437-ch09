import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AminoAcid {
    private String name;
    private String threeLetter;
    private String oneLetter;
    //private ArrayList<String> codons = new ArrayList<>();
    private String[] codons;
    public AminoAcid(String n, String tL, String oL, String[] s)
    {
        name = n; threeLetter = tL; oneLetter = oL;
        //make copy of incoming string
        codons = new String[s.length];
        for (int i = 0; i < s.length; i++)
            codons[i] = s[i];
    }

    public AminoAcid(AminoAcid other)
    {
        this.name = other.name;
        this.oneLetter = other.oneLetter;
        this.threeLetter = other.threeLetter;
        //make copy of array
        codons = new String[other.codons.length];
        for (int i = 0; i < codons.length; i++)
            codons[i] = other.codons[i];
    }

    public String getName() { return name;}
    public String getThreeLetter() { return threeLetter;}
    public String getOneLetter() { return oneLetter;}
    public String[] getCodons()
    {
        return codons;
    }

    public String toString()
    {
        return name + "(" + oneLetter + "): " + codons.toString();
    }

    /* @param c - the codon to test
    @return true if this AA contains the codon. False if not.
     */
    public boolean contains(String c)
    {
        boolean retVal = false;
        for (int i = 0; i < codons.length; i++)
            if (codons[i].equals(c))
                retVal = true;
        return retVal;
    }

    //equals
    public boolean equals(AminoAcid other)
    {
        return this.name.equals(other.name) && this.oneLetter.equals(other.oneLetter);
    }
    //clone
    public AminoAcid clone()
    {
        return new AminoAcid(this);
    }
}
