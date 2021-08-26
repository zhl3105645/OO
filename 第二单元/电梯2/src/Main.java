import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<PersonRequest> waitQueue = new ArrayList<>();
        ArrayList<Elevator> elevators = new ArrayList<>();
        int i;
        for (i = 0; i < 3; i++) {
            Elevator elevator = new Elevator(waitQueue, i + 1);
            elevators.add(elevator);
        }
        WaitQueue waitQueue1 = new WaitQueue(waitQueue, elevators);
        waitQueue1.start();
    }
}
