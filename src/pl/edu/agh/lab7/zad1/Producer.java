package pl.edu.agh.lab7.zad1;

import java.util.Random;

public class Producer extends Thread{
    private int id;
    private Proxy proxy;
    private Random rand;

    Producer(int id, Proxy proxy){
        this.id = id;
        this.proxy = proxy;
        rand = new Random();
    }

    @Override
    public void run(){
        while(true){
            int tmp = rand.nextInt(100);
            proxy.add(tmp);
            System.out.println("Producer " + id + " added: " + tmp + " to the buffer");
            try {
                Thread.sleep(rand.nextInt(300));
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
