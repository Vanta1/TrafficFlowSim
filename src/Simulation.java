import java.util.Scanner;

public class Simulation {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        StreetMap streetMap = new StreetMap("testmap.xml", Integer.parseInt(args[0])); // TODO: StreetMap streetMap = new StreetMap(scan.nextLine());
        LogSystem logSystem = new LogSystem();
        System.out.println("Looking good!");
        scan.close();
    }
}
