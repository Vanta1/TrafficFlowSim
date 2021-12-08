import java.util.ArrayList;

public class Car {
    private ArrayList<Integer> route;
    private int[] startRoute;
    private int routeLength = 0;
    private int accel;

    public Car(int startNode, int endNode, int accel) {
        this.startRoute = new int[2];
        this.startRoute[0] = startNode;
        this.startRoute[1] = endNode;
        this.accel = accel;
    }

    public void setStartRoute(int[] startRoute) {
        this.startRoute = startRoute;
    }

    public void setRoute(ArrayList<Integer> newRoute, int routeLength) {
        this.route = newRoute;
        this.routeLength = routeLength;
    }

    public int[] getStartRoute() {
        return this.startRoute;
    }

    public int getRouteLength() {
        return routeLength;
    }

    public int getAccel() {
        return accel;
    }
}
