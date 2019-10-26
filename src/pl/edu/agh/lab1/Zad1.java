package pl.edu.agh.lab1;

public class Zad1 {

    public static Counter a = new Counter(0);
    public static int maxVal = 100000;

    public static void main(String[] args) throws Exception {

        Thread t1 = new Thread(() -> {
            for(int i = 0; i < maxVal; i++) {
                a.inc();
            }
        });
        Thread t2 = new Thread(() -> {
            for(int i = 0; i < maxVal; i++) {
                a.dec();
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(a.value());
    }
}
