import java.sql.Array;
import java.util.ArrayList;

public class AminoAcid {
    private String name;
    private String threeLetter;
    private String oneLetter;
    //private ArrayList<String> codons = new ArrayList<>();
    private String[] codons;
    public AminoAcid(String n, String tL, String oL, String[] s)
    {
        name = n; threeLetter = tL; oneLetter = oL;
        codons = s;
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

    public boolean contains(String c)
    {
        boolean retVal = false;
        for (int i = 0; i < codons.length; i++)
            if (codons[i].equals(c))
                retVal = true;
        return retVal;
    }

}
