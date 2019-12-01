package pl.edu.agh.lab7.zad1;

public class AddRequest implements MethodRequest {
    private Buffer buffer;
    private Object object;

    AddRequest(Buffer buffer, Object object){
        this.buffer = buffer;
        this.object = object;
    }

    @Override
    public void call() {
        buffer.add(object);
    }

    @Override
    public boolean guard() {
        return !buffer.isFull();
    }
}
