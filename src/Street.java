public class Street {
    private int length;
    private int[] connections = new int[2];

    public Street(int length, int[] connections) {
        this.length = length;
        this.connections = connections;
    }

    public int getLength() {
        return length;
    }

    public int[] getConnections() {
        return connections;
    }
}
