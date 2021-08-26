import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        WaitQueue waitQueue = new WaitQueue();
        ArrayList<PersonRequest> requests = waitQueue.getWaitQueue();
        Elevator elevator = new Elevator(requests);
        elevator.start();
        waitQueue.start();
    }
}
