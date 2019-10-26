package pl.edu.agh.lab3.zad2;

class Semafor {
    private boolean _stan = true;
    private int _czeka = 0;

    public Semafor() {
        this._stan = true;
        this._czeka = 0;
    }

    public synchronized void P() {
        _czeka++;
        while(!_stan) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        _czeka--;
        _stan = false;
    }

    public synchronized void V() {
        if(_czeka > 0) {
            this.notify();
        }
        _stan = true;
    }
}

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
    private Semafor s = new Semafor();

    Buffer(int cap) {
        capacity = cap;
        tab = new int[cap];
    }

    void put(int i) {
        s.P();
        while(value >= capacity) {
            s.V();
            s.P();
        }
        tab[value] = i;
        value++;
        s.V();
    }

    int get() {
        s.P();
        while(value <= 0) {
            s.V();
            s.P();
        }
        value--;
        int a = tab[value];
        s.V();
        return a;
    }
}

public class Zad2 {
    public static void main(String[] args) {

        int n1 = 5;
        int n2 = 5;
        int cap = 5;

        Producer[] ps = new Producer[n1];
        Consumer[] cs = new Consumer[n2];

        Buffer buf = new Buffer(cap);

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