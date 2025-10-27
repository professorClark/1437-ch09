import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class NameRandomizer {

    public static void main(String[] args) {
        ArrayList<String> names = readNamesFromCSV("namesTR.csv");
        Collections.shuffle(names);
        ArrayList<String[]> pairs = createPairs(names);
        displayPairs(pairs);
    }

    private static ArrayList<String> readNamesFromCSV(String fileName) {
        ArrayList<String> names = new ArrayList<>();
        try (Scanner infile = new Scanner(new File(fileName))) {
            String line;
            while (infile.hasNext()) {
                line = infile.nextLine();
                names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }

    private static ArrayList<String[]> createPairs(ArrayList<String> names) {
        ArrayList<String[]> pairs = new ArrayList<>();
        for (int i = 0; i < names.size() - 1; i += 2) {
            pairs.add(new String[]{names.get(i), names.get(i + 1)});
        }
        if (names.size() % 2 != 0) {
            pairs.add(new String[]{names.get(names.size() - 1), "No Pair"});
        }
        return pairs;
    }

    private static void displayPairs(ArrayList<String[]> pairs) {
        JFrame frame = new JFrame("Name Pairs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new GridLayout(pairs.size(), 1));

        for (String[] pair : pairs) {
            JLabel label = new JLabel(pair[0] + " - " + pair[1]);
            frame.add(label);
        }

        frame.setVisible(true);
    }
}
