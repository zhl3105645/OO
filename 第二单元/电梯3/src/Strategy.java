import java.util.ArrayList;

public class Strategy {
    public Ask moveTo(ArrayList<Ask> passenger,
                      ArrayList<Ask> wait, String pattern, String type) {
        Ask request = null;//主请求
        synchronized (wait) {
            if (wait.isEmpty() && passenger.isEmpty()) {
                return null;
            }
            switch (pattern) {
                case "Random":
                    if (!passenger.isEmpty()) {
                        request = passenger.get(0);
                    } else {
                        request = hasAskWait(wait, type);
                    }
                    break;
                case "Night":
                    if (passenger.isEmpty()) {
                        int max = 1;
                        for (Ask ask : wait) {
                            if (fit(ask, type) && ask.getFromFloor() > max) {
                                request = ask;
                                max = ask.getFromFloor();
                            }
                        }
                    } else {
                        request = passenger.get(0);
                    }
                    break;
                case "Morning":
                    int max1 = 1;
                    if (passenger.isEmpty()) {
                        for (Ask ask : wait) {
                            if (fit(ask, type) && ask.getToFloor() > max1) {
                                request = ask;
                                max1 = ask.getFromFloor();
                            }
                        }
                    } else {
                        request = passenger.get(0);
                    }
                    break;
                default:
            }
        }
        return request;
    }

    //等待队列中是否含有电梯可以支持的请求
    public Ask hasAskWait(ArrayList<Ask> wait, String type) {
        synchronized (wait) {
            for (Ask ask : wait) {
                switch (type) {
                    case "A":
                        return ask;
                    case "B":
                        if (ask.getFromFloor() % 2 == 1
                                && Math.abs(ask.getFromFloor() - ask.getToFloor()) != 1) {
                            return ask;
                        }
                        break;
                    case "C":
                        if (ask.getFromFloor() <= 2 || ask.getFromFloor() >= 19
                                || ((ask.getFromFloor() == 3 || ask.getFromFloor() == 18)
                                && !(ask.getToFloor() > 3 && ask.getToFloor() < 18))) {
                            return ask;
                        }
                        break;
                    default:
                }
            }
        }
        return null;
    }

    boolean fit(Ask ask, String type) {
        switch (type) {
            case "A":
                return true;
            case "B":
                if (ask.getFromFloor() % 2 == 1
                        && Math.abs(ask.getFromFloor() - ask.getToFloor()) != 1) {
                    return true;
                }
                break;
            case "C":
                if (ask.getFromFloor() <= 2 || ask.getFromFloor() >= 19
                        || ((ask.getFromFloor() == 3 || ask.getFromFloor() == 18)
                        && !(ask.getToFloor() > 3 && ask.getToFloor() < 18))) {
                    return true;
                }
                break;
            default:
        }
        return false;
    }
}

