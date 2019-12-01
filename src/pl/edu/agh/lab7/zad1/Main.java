package pl.edu.agh.lab7.zad1;

public class Main {
    private final static int pn = 3;
    private final static int cn = 5;
    private final static int ao = 10;

    public static void main(String[] argv) throws InterruptedException {
        ActiveObject activeObject = new ActiveObject(ao);
        Proxy proxy = activeObject.getProxy();
        Producer[] producers = new Producer[pn];
        Consumer[] consumers = new Consumer[cn];

        for (int i = 0; i < pn; i++) {
            producers[i] = new Producer(i, proxy);
        }

        for (int i = 0; i < cn; i++) {
            consumers[i] = new Consumer(i, proxy);
        }

        for (int i = 0; i < cn; i++) {
            consumers[i].start();
        }

        for (int i = 0; i < pn; i++) {
            producers[i].start();
        }

        for (int i = 0; i < pn; i++) {
            producers[i].join();
        }

        for (int i = 0; i < cn; i++) {
            consumers[i].join();
        }
    }
}
