package pl.edu.agh.lab6.zad3;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Lokaj extends Thread {
    Lock lock = new ReentrantLock();
    Queue<Filozof> queue = new ConcurrentLinkedQueue<>();
    int widelce = 5;

    void popros(Filozof f) {
        lock.lock();
        try {
            f.s.acquire();
        } catch (InterruptedException e) { e.printStackTrace(); }
        queue.add(f);
        lock.unlock();
    }

    void oddaj(Filozof f) {
        lock.lock();
        widelce+=2;
        f.s.release();
        lock.unlock();
    }

    public void run() {
        while (true) {
            if(!queue.isEmpty() && widelce >= 2) {
                lock.lock();
                Filozof f = queue.peek();
                queue.remove();
                widelce-=2;
                f.s.release();
                lock.unlock();
            }
        }
    }
}

class Filozof extends Thread {
    Semaphore s = new Semaphore(1);
    private int _licznik = 0;
    Lokaj l;

    Filozof(Lokaj l) {
        this.l = l;
    }

    public void run() {
        while (true) {
            l.popros(this);
            try {
                s.acquire();
            } catch (InterruptedException e) { e.printStackTrace(); }
            ++_licznik;
            if (_licznik % 100 == 0) {
                System.out.println("Filozof: " + Thread.currentThread() + "jadlem " + _licznik + " razy");
            }
            l.oddaj(this);
            if(_licznik == 10000000) {
                System.out.println("Done");
                return;
            }
        }
    }
}

public class Zad3 {
    public static void main(String[] args) {
        Lokaj l = new Lokaj();
        Filozof[] fs = new Filozof[5];
        for(int i = 0; i < 5; i++) {
            fs[i] = new Filozof(l);
        }
        Long start = System.nanoTime();
        l.start();
        for(int i = 0; i < 5; i++) {
            fs[i].start();
        }
        for(int i = 0; i < 5; i++) {
            try {
                fs[i].join();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
        Long end = System.nanoTime();
        System.out.println(end-start);
    }
}