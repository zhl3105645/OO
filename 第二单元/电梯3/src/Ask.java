public class Ask {
    private int fromFloor;
    private int toFloor;
    private int id;

    public Ask(int fromFloor, int toFloor, int id) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.id = id;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public int getId() {
        return id;
    }
}
