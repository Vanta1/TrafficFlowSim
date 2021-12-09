import java.util.Scanner;

public class Simulation {
    public static void main(String[] args) {
        StreetMap streetMap = new StreetMap(args[1], Integer.parseInt(args[2]), args[0].equals("r"));
    }
}
