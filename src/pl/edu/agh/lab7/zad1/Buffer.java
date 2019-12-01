package pl.edu.agh.lab7.zad1;

import java.util.LinkedList;
import java.util.Queue;

class Buffer {
    private int bufSize;
    private Queue<Object> buffer;
    Buffer(int bufSize){
        this.bufSize = bufSize;
        this.buffer = new LinkedList<Object>();
    }
    void add(Object object) {
        if(!this.isFull()){
            this.buffer.add(object);
        }
    }
    Object remove() {
        if(this.isEmpty()){
            return null;
        }
        else{
            return buffer.remove();
        }
    }
    boolean isFull() {
        return buffer.size() == bufSize;
    }
    boolean isEmpty() {
        return buffer.isEmpty();
    }
}
