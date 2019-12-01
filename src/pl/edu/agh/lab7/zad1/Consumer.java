package pl.edu.agh.lab7.zad1;

import java.util.Random;

public class Consumer extends Thread{
    private int id;
    private Proxy proxy;

    Consumer(int id, Proxy proxy){
        this.id = id;
        this.proxy = proxy;
    }

    @Override
    public void run(){
        while(true){
            Random rand = new Random();
            ProxyFuture consumed = proxy.remove();
            while(!consumed.isReady()){
                System.out.println("Consumer " + id + " is waiting.");
                try {
                    Thread.sleep(rand.nextInt(400));
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
            System.out.println("Consumer " + id + " removed: "
                    + consumed.getObject() + "from the buffer");
            try {
                Thread.sleep(rand.nextInt(400));
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
