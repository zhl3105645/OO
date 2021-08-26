public class Edge implements Comparable<Edge> {
    private int to;
    private int value;

    public Edge(int to, int value) {
        this.to = to;
        this.value = value;
    }

    @Override
    public int compareTo(Edge o) {
        return this.value - o.value;
    }

    public int getTo() {
        return to;
    }

    public int getValue() {
        return value;
    }
}
