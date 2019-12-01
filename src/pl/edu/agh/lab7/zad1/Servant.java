package pl.edu.agh.lab7.zad1;

public class Servant implements Proxy {
    Buffer buffer;
    Scheduler scheduler;

    Servant(Buffer buffer, Scheduler scheduler){
        this.buffer = buffer;
        this.scheduler = scheduler;
    }

    @Override
    public void add(Object object){
        scheduler.enqueue(new AddRequest(buffer, object));
    }

    @Override
    public ProxyFuture remove(){
        ProxyFuture proxyFuture = new ProxyFuture();
        scheduler.enqueue(new RemoveRequest(buffer, proxyFuture));
        return proxyFuture;
    }
}
