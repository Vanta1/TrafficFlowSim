import java.util.ArrayList;

public class Car {
    private ArrayList<Integer> route;
    private int[] startRoute;
    private int routeLength;
    private int accel;

    public void setRoute(ArrayList<Integer> route) {
        this.route = route;
    }

    public void setRouteLength(int routeLength) {
        this.routeLength = routeLength;
    }

    public void setAccel(int accel) {
        this.accel = accel;
    }

    public Car() {
        this.route = new ArrayList<>();
        this.startRoute = new int[2];
        this.routeLength = 0;
        this.accel = 0;
    }

    public ArrayList<Integer> getRoute() {
        return route;
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
