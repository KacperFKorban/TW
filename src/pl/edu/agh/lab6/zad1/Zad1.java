package pl.edu.agh.lab6.zad1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Widelec {
    Lock lock = new ReentrantLock();
    public void podnies() {
        lock.lock();
    }
    public void odloz() {
        lock.unlock();
    }
}

class Filozof extends Thread {
    private int _licznik = 0;
    Widelec l;
    Widelec r;

    public Filozof(Widelec l, Widelec r) {
        this.l = l;
        this.r = r;
    }

    public void run() {
        while (true) {
            l.podnies();
            r.podnies();
            ++_licznik;
            if (_licznik % 100 == 0) {
                System.out.println("zad1.Filozof: " + Thread.currentThread() +
                        "jadlem " + _licznik + " razy");
            }
            l.odloz();
            r.odloz();
        }
    }
}

public class Zad1 {
    public static void main(String[] args) {
        Widelec[] ws = new Widelec[5];
        Filozof[] fs = new Filozof[5];
        for(int i = 0; i < 5; i++) {
            ws[i] = new Widelec();
        }
        for(int i = 0; i < 5; i++) {
            fs[i] = new Filozof(ws[i], ws[(i+1)%5]);
        }
        for(int i = 0; i < 5; i++) {
            fs[i].start();
        }
    }
}