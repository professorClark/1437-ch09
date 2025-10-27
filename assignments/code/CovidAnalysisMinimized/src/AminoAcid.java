import java.util.Arrays;

public class AminoAcid {
    private final String name;
    private final String oneLetter;
    private final String[] codons;
    public AminoAcid(String n, String oL, String[] s)
    {
        name = n; oneLetter = oL;
        codons = s;
    }

    public String getName() { return name;}
    public String getOneLetter() { return oneLetter;}
    public String[] getCodons()
    {
        return codons;
    }

    public String toString()
    {
        return name + "(" + oneLetter + "): " + Arrays.toString(codons);
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
