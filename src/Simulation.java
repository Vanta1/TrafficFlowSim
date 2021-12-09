import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Simulation {
    public static void main(String[] args) {
        StreetMap streetMap = null;
        FileWriter dataWriter = null;
        try {
            dataWriter = new FileWriter("dataOut.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < Integer.parseInt(args[3]); i++) {
            streetMap = new StreetMap(args[1], Integer.parseInt(args[2]), args[0].equals("r"), dataWriter);
        }
        try {
            dataWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
