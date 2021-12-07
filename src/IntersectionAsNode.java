public class IntersectionAsNode {
    private int id;
    private int connectLength;
    private boolean visited = false;
    private int parentId;

    public int getId() {
        return id;
    }

    public int getConnectLength() {
        return connectLength;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getParentId() {
        return parentId;
    }

    public IntersectionAsNode(int id, int connectLength, int parentId) {
        this.id = id;
        this.connectLength = connectLength;
        this.parentId = parentId;
    }
}
