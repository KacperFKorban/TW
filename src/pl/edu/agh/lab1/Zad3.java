package pl.edu.agh.lab1;

public class Zad3 {

    public static Counter a = new Counter(0);
    public static int mutex = 0;
    public static int maxVal = 10000;
    public static int millis = 0;
    public static int nanos = 10;

    public static void main(String[] args) throws Exception {

        Thread t1 = new Thread(() -> {
            try {
                for(int i = 0; i < maxVal; i++) {
                    Thread.sleep(millis, nanos);
                    while(mutex < 0) {
                        Thread.sleep(millis, nanos);
                    }
                    mutex--;
                    System.out.println("Inc: " + i);
                    a.inc();
                    mutex++;
                }
            } catch (Exception e) {}
        });
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(millis, nanos);
                for(int i = 0; i < maxVal; i++) {
                    Thread.sleep(millis, nanos);
                    while(mutex < 0) {
                        Thread.sleep(millis, nanos);
                    }
                    mutex--;
                    System.out.println("Dec: " + i);
                    a.dec();
                    mutex++;
                }
            } catch (Exception e) {}
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(a.value());
    }
}
