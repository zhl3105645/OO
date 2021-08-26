import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Process process = new Process();
        String line = scanner.nextLine();
        line = process.manageLine(line);
        //System.out.println("Line: " + line);
        Poly poly = new Poly();

        ArrayList<Item> items = process.getItems(line);
        for (Item item : items) {
            poly.addItem(item);
        }
        poly.polyDer();
        //System.out.println(poly.derToString());
        ArrayList<Item> polyDer = poly.getItemsDer();
        if (polyDer.isEmpty()) {
            System.out.println("0");
        } else {
            Simply simply = new Simply();
            ArrayList<String> itemStr = simply.simply(polyDer);
            String s = simply.mergeStr(itemStr);
            System.out.println(s);
        }

    }
}
