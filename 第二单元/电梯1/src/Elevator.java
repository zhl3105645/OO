import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Elevator extends Thread {
    private static final int MAXFLOOR = 20;
    private static final int MINFLOOR = 1;
    private static final int MAXSIZE = 6;
    private int floor;//当前楼层
    private int direct;//1->up;0->down
    private static boolean end = false;
    private static String type;
    private ArrayList<PersonRequest> passenger = new ArrayList<>();
    private ArrayList<PersonRequest> waitQueue;
    private Strategy strategy = new Strategy();

    public Elevator(ArrayList<PersonRequest> waitQueue) {
        this.floor = 1;
        this.direct = 1;
        this.waitQueue = waitQueue;
    }

    public static void setEnd(boolean end) {
        Elevator.end = end;
    }

    public static void setType(String type) {
        Elevator.type = type;
    }

    @Override
    public void run() {
        TimableOutput.initStartTimestamp();
        while (true) {
            synchronized (waitQueue) {
                if (end && passenger.isEmpty() && waitQueue.isEmpty()) {
                    break;
                } else if (passenger.isEmpty() && waitQueue.isEmpty()) {
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            synchronized (waitQueue) {
                if (end && passenger.isEmpty() && waitQueue.isEmpty()) {
                    break;
                }
            }
            if (type.equals("Morning") && passenger.isEmpty()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (type.equals("Night") && passenger.isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            PersonRequest targetRequest = strategy.moveTo(passenger, waitQueue, type);
            int target;
            if (passenger.contains(targetRequest)) {
                target = targetRequest.getToFloor();
            } else {
                target = targetRequest.getFromFloor();
            }
            if (target == floor) {
                outAndIn(targetRequest);
                continue;
            }
            direct = (target > floor) ? 1 : 0;
            outAndIn(targetRequest);
            move(targetRequest, target);
        }
    }

    void move(PersonRequest targetRequest, int target) {
        if (direct == 1) {
            while (floor < target) {
                up();
                outAndIn(targetRequest);
            }
        } else if (direct == 0) {
            while (floor > target) {
                down();
                outAndIn(targetRequest);
            }
        }
    }

    void outAndIn(PersonRequest targetRequest) {
        ArrayList<PersonRequest> personOff = getOff();
        ArrayList<PersonRequest> personIn = getIn(targetRequest);
        if (!(personIn.isEmpty() && personOff.isEmpty())) {
            open();
            for (PersonRequest personRequest : personOff) {
                TimableOutput.println("OUT-" + personRequest.getPersonId() + "-" + floor);
            }
            for (PersonRequest personRequest : personIn) {
                TimableOutput.println("IN-" + personRequest.getPersonId() + "-" + floor);
            }
            close();
        }
    }

    ArrayList<PersonRequest> getIn(PersonRequest targetRequest) {
        ArrayList<PersonRequest> personIn = new ArrayList<>();
        synchronized (waitQueue) {
            for (PersonRequest personRequest : waitQueue) {
                if (personRequest.getFromFloor() == floor && passenger.size() < MAXSIZE) {
                    if ((personRequest.equals(targetRequest))
                            || (direct == 1 && (personRequest.getToFloor() > floor))
                            || (direct == 0 && (personRequest.getToFloor() < floor))) {
                        passenger.add(personRequest);
                        personIn.add(personRequest);
                    }
                }
            }
            for (PersonRequest personRequest : personIn) {
                waitQueue.remove(personRequest);
            }
        }
        return personIn;
    }

    ArrayList<PersonRequest> getOff() {
        ArrayList<PersonRequest> personOff = new ArrayList<>();
        for (PersonRequest personRequest : passenger) {
            if (personRequest.getToFloor() == floor) {
                personOff.add(personRequest);
            }
        }
        for (PersonRequest personRequest : personOff) {
            passenger.remove(personRequest);
        }
        return personOff;
    }

    void up() {
        if (floor < MAXFLOOR) {
            try {
                floor++;
                Thread.sleep(400);
                TimableOutput.println("ARRIVE-" + floor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ERROR");
        }
    }

    void down() {
        if (floor > MINFLOOR) {
            try {
                floor--;
                Thread.sleep(400);
                TimableOutput.println("ARRIVE-" + floor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ERROR");
        }
    }

    void open() {
        try {
            TimableOutput.println("OPEN-" + floor);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void close() {
        try {
            Thread.sleep(200);
            TimableOutput.println("CLOSE-" + floor);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
