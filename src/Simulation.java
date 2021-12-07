import java.util.Scanner;

public class Simulation {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        StreetMap streetMap = new StreetMap(args[0], Integer.parseInt(args[1])); // TODO: StreetMap streetMap = new StreetMap(scan.nextLine());
        LogSystem logSystem = new LogSystem();
        System.out.println("Looking good!");
        scan.close();
    }
}
