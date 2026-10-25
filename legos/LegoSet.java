public record LegoSet(String name, int year, int numParts)
{
    //remember, we get all of the "get" methods for free with a record
    //we also get a toString, but it's not great. Let's build our own.
    //and if you hate this one, change it to something else.
    public String toString()
    {
        return ("Set name: " + name + ", Year: " + year + ", Number of Parts: " + numParts);
    }

}
