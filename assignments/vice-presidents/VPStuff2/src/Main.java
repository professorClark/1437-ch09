import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("VicePresidentAges.csv");
        Scanner infile = new Scanner(f);
        ArrayList<String> names = new ArrayList<>(50);
        ArrayList<Integer> ages = new ArrayList<>(50);


        while(infile.hasNext())
        {
            String str = infile.nextLine();
            String[] tokens = str.split(",");
            names.add(tokens[0]); //name
            ages.add(Integer.parseInt(tokens[1]));

        }


    }
}