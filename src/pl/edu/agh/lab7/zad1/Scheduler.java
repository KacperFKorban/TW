package pl.edu.agh.lab7.zad1;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Scheduler extends Thread {
    private Queue<MethodRequest> activationQueue;

    Scheduler() {
        activationQueue = new ConcurrentLinkedQueue<MethodRequest>();
    }

    void enqueue(MethodRequest request) {
        activationQueue.add(request);
    }

    public void run() {
        while (true) {
            MethodRequest imr = activationQueue.poll();
            if (imr != null) {
                if (imr.guard()) {
                    imr.call();
                } else {
                    activationQueue.add(imr);
                }
            }
        }
    }
}
