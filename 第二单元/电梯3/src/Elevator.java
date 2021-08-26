import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private static final int MAXFLOOR = 20;
    private static final int MINFLOOR = 1;
    private final int maxSize;
    private final int id;
    private final String type;
    private int floor;//当前楼层
    private int direct;//1->up;0->down
    private static boolean end = false;
    private static String pattern;
    private ArrayList<Ask> passenger = new ArrayList<>();
    private ArrayList<Ask> waitQueue;
    private ArrayList<Ask> passengers;
    private Strategy strategy = new Strategy();
    private WaitQueue threadWait;

    public Elevator(WaitQueue threadWait, ArrayList<Ask> waitQueue,
                    ArrayList<Ask> passengers, int id, String type) {
        this.threadWait = threadWait;
        this.floor = 1;
        this.direct = 1;
        this.waitQueue = waitQueue;
        this.passengers = passengers;
        this.id = id;
        this.type = type;
        this.maxSize = type.equals("A") ? 8 : (type.equals("B") ? 6 : 4);
    }

    public static void setEnd(boolean end) {
        Elevator.end = end;
    }

    public static void setPattern(String pattern) {
        Elevator.pattern = pattern;
    }

    public ArrayList<Ask> getPassenger() {
        return passenger;
    }

    @Override
    public void run() {
        TimableOutput.initStartTimestamp();
        while (true) {
            synchronized (waitQueue) {
                if (end && waitQueue.isEmpty() && threadWait.isEmptyPassenger()) {
                    break;
                } else if (passenger.isEmpty() && strategy.hasAskWait(waitQueue, type) == null) {
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            synchronized (waitQueue) {
                if (end && waitQueue.isEmpty() && threadWait.isEmptyPassenger()) {
                    break;
                }
            }
            if (pattern.equals("Morning") && passenger.isEmpty()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (pattern.equals("Night") && passenger.isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Ask targetRequest;
            synchronized (waitQueue) {
                targetRequest = strategy.moveTo(passenger, waitQueue, pattern, type);
            }
            if (targetRequest != null) {
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
    }

    void move(Ask targetRequest, int target) {
        if (direct == 1) {
            while (floor < target) {
                if (passenger.isEmpty() && strategy.hasAskWait(waitQueue, type) == null) {
                    return;
                }
                up();
                outAndIn(targetRequest);
            }
        } else if (direct == 0) {
            while (floor > target) {
                if (passenger.isEmpty() && strategy.hasAskWait(waitQueue, type) == null) {
                    return;
                }
                down();
                outAndIn(targetRequest);
            }
        }
    }

    void outAndIn(Ask targetRequest) {
        if (type.equals("A")) {
            floorOutAndIn(targetRequest);
        } else if (type.equals("B")) {
            if (floor % 2 == 1) {
                floorOutAndIn(targetRequest);
            }
        } else {
            if (floor <= 3 || floor >= 18) {
                floorOutAndIn(targetRequest);
            }
        }
    }

    void floorOutAndIn(Ask targetRequest) {
        ArrayList<Ask> changeList = new ArrayList<>();
        ArrayList<Ask> personOff = getOff(changeList);
        ArrayList<Ask> personIn = getIn(targetRequest);
        if (!(personIn.isEmpty() && personOff.isEmpty())) {
            open();
            for (Ask ask : personOff) {
                TimableOutput.println(
                        "OUT-" + ask.getId() + "-" + floor + "-" + id);
            }
            if (!changeList.isEmpty()) {
                synchronized (waitQueue) {
                    waitQueue.addAll(changeList);
                    waitQueue.notifyAll();
                }
            }
            for (Ask ask : personIn) {
                TimableOutput.println("IN-" + ask.getId() + "-" + floor + "-" + id);
            }
            close();
        }
    }

    ArrayList<Ask> getIn(Ask targetRequest) {
        ArrayList<Ask> personIn = new ArrayList<>();
        synchronized (waitQueue) {
            for (Ask ask : waitQueue) {
                if (ask.getFromFloor() == floor && passenger.size() < maxSize) {
                    if ((ask.equals(targetRequest))
                            || (direct == 1 && (ask.getToFloor() > floor))
                            || (direct == 0 && (ask.getToFloor() < floor))) {
                        if (type.equals("C") && (floor == 3 || floor == 18) &&
                                ask.getToFloor() > 3 && ask.getToFloor() < 18) {
                            continue;
                        } else if (type.equals("B")
                                && Math.abs(floor - ask.getToFloor()) == 1) {
                            continue;
                        }
                        passenger.add(ask);
                        passengers.add(ask);
                        personIn.add(ask);
                    }
                }
            }
            for (Ask ask : personIn) {
                waitQueue.remove(ask);
            }
            waitQueue.notifyAll();
        }
        return personIn;
    }

    ArrayList<Ask> getOff(ArrayList<Ask> changeList) {
        ArrayList<Ask> personOff = new ArrayList<>();
        for (Ask ask : passenger) {
            if (ask.getToFloor() == floor) {
                personOff.add(ask);
            } else if (type.equals("B") && Math.abs(floor - ask.getToFloor()) == 1) {
                ask.setFromFloor(floor);
                personOff.add(ask);
                changeList.add(ask);
            } else if (type.equals("C") && (floor == 3 || floor == 18)
                    && ask.getToFloor() > 3 && ask.getToFloor() < 18) {
                ask.setFromFloor(floor);
                personOff.add(ask);
                changeList.add(ask);
            }
        }
        for (Ask ask : personOff) {
            passenger.remove(ask);
            passengers.remove(ask);
        }
        return personOff;
    }

    void up() {
        if (floor < MAXFLOOR) {
            try {
                floor++;
                if (type.equals("A")) {
                    Thread.sleep(600);
                } else if (type.equals("B")) {
                    Thread.sleep(400);
                } else {
                    Thread.sleep(200);
                }
                TimableOutput.println("ARRIVE-" + floor + "-" + id);
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
                if (type.equals("A")) {
                    Thread.sleep(600);
                } else if (type.equals("B")) {
                    Thread.sleep(400);
                } else {
                    Thread.sleep(200);
                }
                TimableOutput.println("ARRIVE-" + floor + "-" + id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ERROR");
        }
    }

    void open() {
        try {
            TimableOutput.println("OPEN-" + floor + "-" + id);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void close() {
        try {
            Thread.sleep(200);
            TimableOutput.println("CLOSE-" + floor + "-" + id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
