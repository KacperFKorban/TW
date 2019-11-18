package pl.edu.agh.lab5.zad1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class Reader extends Thread {
    private Semaphore ra;
    private Semaphore ca;
    private Semaphore sq;
    private AtomicInteger rc;
    Reader(Semaphore ra, Semaphore ca, Semaphore
            sq, AtomicInteger rc) {
        this.ra = ra;
        this.ca = ca;
        this.sq = sq;
        this.rc = rc;
    }
    @Override
    public void run() {
        try {
            sq.acquire();
            ca.acquire();
            if (rc.get() == 0) {
                ra.acquire();
            }
            rc.addAndGet(1);
            sq.release();
            ca.release();
            Thread.sleep(400);
            ca.acquire();
            rc.addAndGet(-1);
            if(rc.get() == 0) {
                ra.release();
            }
            ca.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Writer extends Thread {
    private Semaphore ra;
    private Semaphore sq;
    Writer(Semaphore ra, Semaphore ca, Semaphore
            sq) {
        this.ra = ra;
        this.sq = sq;
    }
    @Override
    public void run() {
        try {
            sq.acquire();
            ra.acquire();
            sq.release();
            Thread.sleep(400);
            ra.release();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Zad1 {
    public static void main(String[] args) throws Exception {
        Semaphore ra = new Semaphore(1);
        Semaphore ca = new Semaphore(1);
        Semaphore sq = new Semaphore(1);
        AtomicInteger readCount = new AtomicInteger(0);
        for(int k = 1; k <= 10; k++) {
            for(int l = 10; l <= 100; l+=10) {
                Reader[] readers = new Reader[l];
                Writer[] writers = new Writer[k];
                long start = System.nanoTime();
                for (int i = 0; i < l; i++) {
                    readers[i] = new Reader(ra,
                            ca, sq, readCount);
                    readers[i].start();
                }
                for (int i = 0; i < k; i++) {
                    writers[i] = new Writer(ra, ca,
                            sq);
                    writers[i].start();
                }
                for (int i = 0; i < l; i++) {
                    readers[i].join();
                }
                for (int i = 0; i < k; i++) {
                    writers[i].join();
                }
                long end = System.nanoTime();
                System.out.println(k + " " + l + " " + (end - start));
            }
        }
    }
}