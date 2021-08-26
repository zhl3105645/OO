import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

public class WaitQueue extends Thread {
    private ArrayList<PersonRequest> waitQueue;
    private ArrayList<Elevator> elevators;
    private static final boolean DEBUG = false;

    public WaitQueue(ArrayList<PersonRequest> waitQueue, ArrayList<Elevator> elevators) {
        this.waitQueue = waitQueue;
        this.elevators = elevators;
    }

    @Override
    public void run() {
        for (Elevator elevator : elevators) {
            elevator.start();
        }
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
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                if (request instanceof PersonRequest) {
                    synchronized (waitQueue) {
                        waitQueue.add((PersonRequest) request);
                        waitQueue.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) {
                    String idStr = ((ElevatorRequest) request).getElevatorId();
                    int id = Integer.parseInt(idStr);
                    Elevator elevator;
                    synchronized (waitQueue) {
                        elevator = new Elevator(waitQueue, id);
                    }
                    elevator.start();
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

