package pl.edu.agh.lab7.zad1;

class ActiveObject {
    private Buffer buffer;
    private Scheduler scheduler;
    private Proxy proxy;

    ActiveObject(int queueSize){
        super();
        buffer = new Buffer(queueSize);
        scheduler = new Scheduler();
        proxy = new Servant(buffer, scheduler);
        scheduler.start();
    }

    Proxy getProxy(){
        return this.proxy;
    }
}
