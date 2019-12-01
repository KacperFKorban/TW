package pl.edu.agh.lab7.zad1;

class ProxyFuture {
    private Object object;
    void setObject(Object object){
        this.object = object;
    }
    Object getObject(){
        return object;
    }
    boolean isReady(){
        return object != null;
    }
}
