package pl.edu.agh.lab7.zad1;

public class RemoveRequest implements MethodRequest {
    private Buffer buffer;
    private ProxyFuture proxyFuture;

    RemoveRequest(Buffer buffer, ProxyFuture proxyFuture){
        this.buffer = buffer;
        this.proxyFuture = proxyFuture;
    }

    @Override
    public void call() {
        proxyFuture.setObject(buffer.remove());
    }

    @Override
    public boolean guard() {
        return !buffer.isEmpty();
    }
}
