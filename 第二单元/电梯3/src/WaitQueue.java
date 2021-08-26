import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

public class WaitQueue extends Thread {
    private ArrayList<Ask> waitQueue;
    private ArrayList<Ask> passengers = new ArrayList<>();
    private ArrayList<Elevator> elevators = new ArrayList<>();
    private static final boolean DEBUG = false;
    private String[] strings = {"A", "B", "C"};

    public WaitQueue(ArrayList<Ask> waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            Elevator elevator = new Elevator(this, waitQueue, passengers, i + 1, strings[i]);
            elevators.add(elevator);
        }
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
        Elevator.setPattern(arrivePattern);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                if (request instanceof PersonRequest) {
                    synchronized (waitQueue) {
                        PersonRequest request1 = (PersonRequest) request;
                        Ask ask = new Ask(request1.getFromFloor(),
                                request1.getToFloor(), request1.getPersonId());
                        waitQueue.add(ask);
                        waitQueue.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) {
                    String idStr = ((ElevatorRequest) request).getElevatorId();
                    int id = Integer.parseInt(idStr);
                    String type = ((ElevatorRequest) request).getElevatorType();
                    Elevator elevator;
                    synchronized (waitQueue) {
                        elevator = new Elevator(this, waitQueue, passengers, id, type);
                        elevators.add(elevator);
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

    public boolean isEmptyPassenger() {
        for (Elevator elevator : elevators) {
            ArrayList<Ask> asks = elevator.getPassenger();
            if (!asks.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
