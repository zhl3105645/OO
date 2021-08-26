import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Strategy {
    public PersonRequest moveTo(ArrayList<PersonRequest> passenger,
                                ArrayList<PersonRequest> wait, String type) {
        PersonRequest request = null;//主请求
        if (wait.isEmpty() && passenger.isEmpty()) {
            return request;
        }
        synchronized (wait) {
            switch (type) {
                case "Random":
                    if (!passenger.isEmpty()) {
                        request = passenger.get(0);
                        return request;
                    } else {
                        request = wait.get(0);
                    }
                    break;
                case "Night":
                    if (passenger.isEmpty()) {
                        int max = 1;
                        for (PersonRequest personRequest : wait) {
                            if (personRequest.getFromFloor() > max) {
                                request = personRequest;
                                max = personRequest.getFromFloor();
                            }
                        }
                    } else {
                        request = passenger.get(0);
                    }
                    break;
                case "Morning":
                    int max1 = 1;
                    if (passenger.isEmpty()) {
                        for (PersonRequest personRequest : wait) {
                            if (personRequest.getToFloor() > max1) {
                                request = personRequest;
                                max1 = personRequest.getFromFloor();
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
}
