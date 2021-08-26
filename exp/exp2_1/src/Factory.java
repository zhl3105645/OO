import java.util.List;

public class Factory {
    public static Vehicle getNew(List<String> ops) {
        String type = ops.get(1);
        int id = Integer.parseInt(ops.get(2));
        int price = Integer.parseInt(ops.get(3));
        if ("Car".equals(type)) {
            // TODO
            return new Car(id, price, Integer.parseInt(ops.get(4)));
        } else if ("Sprinkler".equals(type)) {
            // TODO
            return new Sprinkler(id, price, Integer.parseInt(ops.get(4)));
        } else {
            // TODO
            return new Bus(id, price, Integer.parseInt(ops.get(4)));
        }
    }
}
