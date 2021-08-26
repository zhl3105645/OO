import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Ask> waitQueue = new ArrayList<>();
        WaitQueue waitQueue1 = new WaitQueue(waitQueue);
        waitQueue1.start();
    }
}
