package pl.edu.agh.lab6.zad2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Widelec {
    Lock lock = new ReentrantLock();
    boolean podnies() {
        return lock.tryLock();
    }
    void odloz() {
        lock.unlock();
    }
}

class Filozof extends Thread {
    private int _licznik = 0;
    Widelec l;
    Widelec r;

    Filozof(Widelec l, Widelec r) {
        this.l = l;
        this.r = r;
    }

    public void run() {
        while (true) {
            while(true) {
                boolean lc = l.podnies();
                boolean rc = r.podnies();
                if(lc && rc) break;
                if(lc) l.odloz();
                if(rc) r.odloz();
            }
            ++_licznik;
            if (_licznik % 100 == 0) {
                System.out.println("Filozof: " + Thread.currentThread() + "jadlem " + _licznik + " razy");
            }
            l.odloz();
            r.odloz();
            if(_licznik == 10000000) {
                System.out.println("Done");
                return;
            }
        }
    }
}

public class Zad2 {
    public static void main(String[] args) {
        Widelec[] ws = new Widelec[5];
        Filozof[] fs = new Filozof[5];
        for(int i = 0; i < 5; i++) {
            ws[i] = new Widelec();
        }
        for(int i = 0; i < 5; i++) {
            fs[i] = new Filozof(ws[i], ws[(i+1)%5]);
        }
        Long start = System.nanoTime();
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