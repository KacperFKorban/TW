package pl.edu.agh.lab3.zad3;

import java.util.Random;
import java.util.function.Function;

class BufferCell {
    private int order;
    int value;
    BufferCell(int value) {
        this.order = -1;
        this.value = value;
    }

    synchronized void apply(Function<Integer, Integer> f, int p) {
        while(order + 1 != p) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Worker with order " + p + " changing value " + value + " to " + f.apply(value));
        value = f.apply(value);
        order++;
        notify();
    }
}

class Buffer {
    BufferCell[] cells;
    Buffer(BufferCell[] cells) {
        this.cells = cells;
    }
}

class Worker extends Thread {
    private Function<Integer, Integer> f;
    private Buffer buffer;
    private int order;

    public Worker(Function<Integer, Integer> f, Buffer buffer, int order) {
        this.f = f;
        this.buffer = buffer;
        this.order = order;
    }

    @Override
    public void run() {
        for(int i = 0; i < buffer.cells.length; i++) {
            buffer.cells[i].apply(f, order);
        }
    }
}

public class Zad3 {
    public static void main(String[] args) {
        int n = 5;

        BufferCell[] cells = new BufferCell[n];

        for(int i = 0; i < n; i++) {
            cells[i] = new BufferCell(i);
        }

        Buffer buffer = new Buffer(cells);

        int w = 4;

        Worker[] workers = new Worker[w];

        Random rand = new Random();

        workers[0] = new Worker(x -> x * 10, buffer, 0);
        workers[1] = new Worker(x -> x / 2, buffer, 1);
        workers[2] = new Worker(x -> x - 1, buffer, 2);
        workers[3] = new Worker(x -> x, buffer, 3);

        for(int i = 0; i < w; i++) {
            workers[i].start();
        }

        for(int i = 0; i < w; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
