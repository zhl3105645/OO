import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Process process = new Process();
        //还需要判断是否是合法字符串
        Judge judge = new Judge();
        if (!judge.isLegal(line)) {
            System.out.println("WRONG FORMAT!");
        } else {
            line = process.manageLine(line);
            //System.out.println(line);
            ArrayList<Item> items = process.getItems(line);
            /*for (Item item : items) {
                System.out.println("ItemStr: " + item.toString());
            }*/
            Poly poly = new Poly(items);
            poly.getPolyDer();
            String s = poly.getPolyDerStr();
            s = process.manageLine(s);
            System.out.println(s);
        }
    }
}
