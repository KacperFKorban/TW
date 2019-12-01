package pl.edu.agh.lab8.zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class MandelbrotWidthRunnable implements Runnable {
    private int y;
    private int MAX_ITER;
    private double ZOOM;
    private int width;
    private BufferedImage I;
    private double zx, zy, cX, cY, tmp;

    public MandelbrotWidthRunnable(int y, int MAX_ITER, double ZOOM, int width, BufferedImage I) {
        this.y = y;
        this.MAX_ITER = MAX_ITER;
        this.ZOOM = ZOOM;
        this.width = width;
        this.I = I;
    }

    @Override
    public void run() {
        for (int x = 0; x < width; x++) {
            zx = zy = 0;
            cX = (x - 400) / ZOOM;
            cY = (y - 300) / ZOOM;
            int iter = MAX_ITER;
            while (zx * zx + zy * zy < 4 && iter > 0) {
                tmp = zx * zx - zy * zy + cX;
                zy = 2.0 * zx * zy + cY;
                zx = tmp;
                iter--;
            }
            I.setRGB(x, y, iter | (iter << 8));
        }
    }
}

class Mandelbrot extends JFrame {

    private ExecutorService es;

    private int MAX_ITER;
    private final double ZOOM = 150;
    private BufferedImage I;

    public Mandelbrot(ExecutorService es, int MAX_ITER) {
        super("Mandelbrot Set");
        Long start = System.nanoTime();
        this.es = es;
        this.MAX_ITER = MAX_ITER;
        setBounds(100, 100, 800, 600);
        Future[] fs = new Future[getHeight()];
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < getHeight(); y++) {
            fs[y] = es.submit(new MandelbrotWidthRunnable(y, MAX_ITER, ZOOM, getWidth(), I));
        }
        for(int i = 0; i < getHeight(); i++) {
            try {
                fs[i].get();
            } catch (Exception e) { e.printStackTrace(); }
        }
        Long end = System.nanoTime();
        System.out.println(end - start);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newSingleThreadExecutor();
//        ExecutorService es = Executors.newFixedThreadPool(4);
//        ExecutorService es = Executors.newCachedThreadPool();
//        ExecutorService es = Executors.newWorkStealingPool();
        for(int i = 400; i <= 4000; i+=400)
            new Mandelbrot(es, i).setVisible(true);
    }
}