import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private ArrayList<Item> poly = new ArrayList<>();
    private ArrayList<Item> polyDer = new ArrayList<>();

    public Poly(ArrayList<Item> items) {
        for (Item item : items) {
            if (item.simply()) {
                poly.add(item);
            }
        }
    }

    public void getPolyDer() throws Exception {
        for (Item item : poly) {
            ArrayList<Item> items = item.itemsDer();
            for (Item item1 : items) {
                if (!item1.getPow().getCoe().equals(BigInteger.valueOf(0))) {
                    if (item1.simply()) {
                        polyDer.add(item1);
                    }
                }
            }
        }
    }

    public String getPolyDerStr() {
        if (polyDer.isEmpty()) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (Item item : polyDer) {
            if (flag) {
                sb.append("+").append(item.toString());
            } else {
                sb.append(item.toString());
                flag = true;
            }
        }
        Process process = new Process();
        return process.manageLine(sb.toString());
    }
}
