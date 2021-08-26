import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

public class WaitQueue extends Thread {
    private ArrayList<PersonRequest> waitQueue = new ArrayList<>();
    private static final boolean DEBUG = false;

    public ArrayList<PersonRequest> getWaitQueue() {
        return waitQueue;
    }

    @Override
    public void run() {
        PipedOutputStream myPipeOut = new PipedOutputStream();
        PipedInputStream myPipeIn = new PipedInputStream();
        try {
            myPipeOut.connect(myPipeIn);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        ElevatorInput elevatorInput;
        if (DEBUG) {
            elevatorInput = new ElevatorInput(myPipeIn);
            new Thread(new DebugInput(myPipeOut)).start();
        } else {
            elevatorInput = new ElevatorInput(System.in);
        }

        String arrivePattern = elevatorInput.getArrivingPattern();
        Elevator.setType(arrivePattern);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            } else {
                synchronized (waitQueue) {
                    waitQueue.add(request);
                    waitQueue.notifyAll();
                }
            }
        }
        try {
            elevatorInput.close();
            Elevator.setEnd(true);
            synchronized (waitQueue) {
                waitQueue.notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
