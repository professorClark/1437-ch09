public class Gene {
    private String geneList;
    private int geneStartPosition;
    private int geneEndPosition;

    public Gene(String str, int gs, int ge)
    {
        geneList = str;
        geneEndPosition = ge;
        geneStartPosition = gs;
    }
    public int getLength()
    {
        return geneList.length();
    }

    public String getAASequence()
    {
        return geneList;
    }

    public int getGeneStartPosition() { return geneStartPosition;}
    public int getGeneEndPosition() { return geneEndPosition;}

}
