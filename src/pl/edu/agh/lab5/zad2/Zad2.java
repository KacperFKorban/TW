package pl.edu.agh.lab5.zad2;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Node {
    int d = 5;
    private Object o;
    private Node tail;
    Lock lock = new ReentrantLock();
    Node(Object o, int d) {
        this.o = o;
        this.tail = null;
        this.d = d;
    }
    boolean contains(Object o) {
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) { e.printStackTrace(); }
        if(tail != null) {
            tail.lock.lock();
            if (tail.o == o) {
                lock.unlock();
                tail.lock.unlock();
                return true;
            } else {
                lock.unlock();
                return tail.contains(o);
            }
        } else {
            lock.unlock();
            return false;
        }
    }
    boolean remove(Object o) {
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) { e.printStackTrace(); }
        if(tail != null) {
            tail.lock.lock();
            if (tail.o == o) {
                tail = tail.tail;
                lock.unlock();
                return true;
            } else {
                lock.unlock();
                return tail.remove(o);
            }
        } else {
            lock.unlock();
            return false;
        }
    }
    boolean add(Object o) {
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) { e.printStackTrace(); }
        if(tail == null) {
            tail = new Node(o, d);
            lock.unlock();
            return true;
        } else {
            tail.lock.lock();
            lock.unlock();
            return tail.add(o);
        }
    }
}

public class Zad2 {
    public static void main(String[] args) {
        final int max = 100;
        for(int d = 5; d <= 20; d+=5) {
            final int n = 100;
            Node head = new Node(new Object(), d);
            Object[] pool = new Object[n];
            Thread[] runner = new Thread[max];
            Random rand = new Random();
            Lock lock = new ReentrantLock();

            for (int i = 0; i < n; i++) {
                pool[i] = new Object();
            }

            long start = System.nanoTime();
            for (int i = 0; i < max; i++) {
                if (i <= max/4) {
                    runner[i] = new Thread(() -> {
                        head.lock.lock();
                        head.add(pool[rand.nextInt(n)]);
                    });
                } else if (i <= 3*max/4) {
                    runner[i] = new Thread(() -> {
                        head.lock.lock();
                        head.contains(pool[rand.nextInt(n)]);
                    });
                } else {
                    runner[i] = new Thread(() -> {
                        head.lock.lock();
                        head.remove(pool[rand.nextInt(n)]);
                    });
                }
            }
            for (int i = 0; i < max; i++) {
                runner[i].start();
            }
            for (int i = 0; i < max; i++) {
                try {
                    runner[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long end = System.nanoTime();

            System.out.println(d + " " + (end - start));
        }
    }
}