import java.util.ArrayList;

public class Intersection {
    private int id; // intersections will remain unnamed for now
    private ArrayList<int[]> connections = new ArrayList<int[]>(); // will hold the IDs of all connected intersections

    public Intersection(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void addConnection(int[] con) {
        this.connections.add(con);
    }
}
