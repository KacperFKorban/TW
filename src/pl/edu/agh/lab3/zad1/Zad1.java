package pl.edu.agh.lab3.zad1;

class Producer extends Thread {
    private Buffer _buf;

    Producer(Buffer buf) {
        _buf = buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            _buf.put(i);
        }
    }
}

class Consumer extends Thread {
    private Buffer _buf;

    Consumer(Buffer buf) {
        _buf = buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            System.out.println(_buf.get());
        }
    }
}

class Buffer {
    private int value = 0;
    private int capacity;
    private int[] tab;

    Buffer(int cap) {
        capacity = cap;
        tab = new int[cap];
    }

    synchronized void put(int i) {
        while(value >= capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tab[value] = i;
        value++;
        notify();
    }

    synchronized int get() {
        while(value <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        value--;
        notify();
        return tab[value];
    }
}

public class Zad1 {
    public static void main(String[] args) {

        int n1 = 5;
        int n2 = 5;

        Producer[] ps = new Producer[5];
        Consumer[] cs = new Consumer[5];

        Buffer buf = new Buffer(5);

        for(int i = 0; i < n1; i++) {
            ps[i] = new Producer(buf);
        }
        for(int i = 0; i < n2; i++) {
            cs[i] = new Consumer(buf);
        }

        for(int i = 0; i < n1; i++) {
            ps[i].start();
        }
        for(int i = 0; i < n2; i++) {
            cs[i].start();
        }

        try {
            for(int i = 0; i < n1; i++) {
                ps[i].join();
            }
            for(int i = 0; i < n2; i++) {
                cs[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}