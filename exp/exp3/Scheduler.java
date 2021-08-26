import java.util.ArrayList;

public class Scheduler extends Thread {
    private final WaitQueue waitQueue;
    private final ArrayList<ProcessingQueue> processingQueues;

    public Scheduler(WaitQueue waitQueue,
                    ArrayList<ProcessingQueue> processingQueues) {
        this.waitQueue = waitQueue;
        this.processingQueues = processingQueues;
    }

    @Override
    public void run() {
        ArrayList<Request> temp = new ArrayList<>();
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.isEnd() && waitQueue.noWaiting()) {
                    System.out.println("Schedule over");
                    for (ProcessingQueue processingQueue : processingQueues) {
                        synchronized (processingQueue) {
                            // need to complete (1)
                            processingQueue.notifyAll();
                        }
                    }
                    return;
                }
                if (waitQueue.noWaiting()) {
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    temp.addAll(waitQueue.getRequests());
                    for (int i = 0; i < temp.size(); i++) {
                        Request request = temp.get(i);
                        if (request.getDestination().equals("Beijing")) {
                            synchronized (request) {
                                for (ProcessingQueue processQueue : processingQueues) {
                                    if (processQueue.getId() == 0) {
                                        processQueue.addRequest(request);
                                        break;
                                    }
                                }
                            }
                        } else if (request.getDestination().equals("Domestic")) {
                            synchronized (request) {
                                for (ProcessingQueue processQueue : processingQueues) {
                                    if (processQueue.getId() == 1) {
                                        processQueue.addRequest(request);
                                        break;
                                    }
                                }
                            }
                        } else {
                            synchronized (request) {
                                for (ProcessingQueue processQueue : processingQueues) {
                                    if (processQueue.getId() == 2) {
                                        processQueue.addRequest(request);
                                        break;
                                    }
                                }
                            }
                        }
                        temp.remove(request);
                        i--;
                    }
                    waitQueue.clearQueue();
                }
            }
        }
    }
}


