import java.util.HashMap;

public class Count {
    private int sum;
    private HashMap<Integer, Integer> exception = new HashMap<>();

    public Count() {
        this.sum = 0;
    }

    public int getSum() {
        return sum;
    }

    public int getIdException(int id) {
        if (exception.get(id) == null) {
            return 0;
        } else {
            return exception.get(id);
        }
    }

    public void addSum() {
        sum++;
    }

    public void addIdException(int id) {
        if (exception.get(id) == null) {
            exception.put(id, 1);
        } else {
            exception.put(id, exception.get(id) + 1);
        }
    }
}
