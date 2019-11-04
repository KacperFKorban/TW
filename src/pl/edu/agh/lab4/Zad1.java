package pl.edu.agh.lab4;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Producer extends Thread {
    private final Buffer _buf;
    int M;

    Producer(Buffer buf, int M) {
        _buf = buf;
        this.M = M;
    }

    public void run() {
        Random rand = new Random();
        int n = rand.nextInt(M) + 1;
        for (int i = 0; i < n && (_buf.consumers.n != 0 || _buf.it < 2*M-1); ++i) {
            _buf.put(rand.nextInt(10));
        }
        synchronized (_buf) {
            _buf.producers.dec();
            if (_buf.producers.n == 0)
                _buf.notify();
        }
    }
}

class Consumer extends Thread {
    private final Buffer _buf;
    int M;

    Consumer(Buffer buf, int M) {
        _buf = buf;
        this.M = M;
    }

    public void run() {
        Random rand = new Random();
        int n = rand.nextInt(M) + 1;
        for (int i = 0; i < n && (_buf.producers.n != 0 || _buf.it >= 0); ++i) {
            System.out.println(_buf.get());
        }
        synchronized (_buf) {
            _buf.consumers.dec();
            if (_buf.consumers.n == 0)
                _buf.notify();
        }
    }
}

class Buffer {
    int it = -1;
    private int M;
    private int[] tab;
    Counter producers;
    Counter consumers;

    Buffer(int M, Counter producers, Counter consumers) {
        this.M = M;
        tab = new int[2*M];
        this.producers = producers;
        this.consumers = consumers;
    }

    synchronized void put(int i) {
        while(it >= 2*M-1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(consumers.n == 0 && it >= 2*M-1) {
                notify();
                return;
            }
        }
            it++;
            tab[it] = i;
            notify();
    }

    synchronized int get() {
        while(it < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(producers.n == 0 && it < 0) {
                notify();
                return -1000696969;
            }
        }
        int tmp = tab[it];
        it--;
        notify();
        return tmp;
    }
}

class Counter {
    int n;
    Counter(int n) {
        this.n = n;
    }
    synchronized void dec() {
        n--;
    }
}

public class Zad1 {
    public static void main(String[] args) {

        long start = System.nanoTime();

        final int n1 = 10;
        final int n2 = 10;
        final int M = 50;

        Counter producers = new Counter(n1);
        Counter consumers = new Counter(n2);


        final Buffer buf = new Buffer(M, producers, consumers);

        Producer[] ps = new Producer[n1];
        Consumer[] cs = new Consumer[n2];

        for(int i=0; i<n1; i++)
            ps[i] = new Producer(buf, M);

        for(int i=0; i<n2; i++)
            cs[i] = new Consumer(buf, M);

        for(int i=0; i<n1; i++)
            ps[i].start();

        for(int i=0; i<n2; i++)
            cs[i].start();

        try {
            for (int i = 0; i < n1; i++)
                ps[i].join();
            for (int i = 0; i < n2; i++)
                cs[i].join();
        } catch (InterruptedException e) { e.printStackTrace(); }

        long end = System.nanoTime();

        System.out.println(end-start);
    }
}