import java.util.ArrayList;

public class Car {
    private ArrayList<Integer> route;
    private int[] startRoute;

    public Car(int startNode, int endNode) {
        startRoute = new int[2];
        startRoute[0] = startNode;
        startRoute[1] = endNode;
    }

    public void setRoute(ArrayList<Integer> newRoute) {
        route = newRoute;
    }

    public int[] getStartRoute() {
        return startRoute;
    }
}
