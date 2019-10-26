package pl.edu.agh.lab2.zad1;

class Counter {
  private int _val;
  public Counter(int n) {
    _val = n;
  }
  public void inc() {
    _val++;
  }
  public void dec() {
    _val--;
  }
  public int value() {
    return _val;
  }
}

class Semafor {
  private boolean _stan = true;
  private int _czeka = 0;

  public Semafor() {
    this._stan = true;
    this._czeka = 0;
  }

  public synchronized void P() throws InterruptedException {
    _czeka++;
    while(!_stan) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    _czeka--;
    _stan = false;
  }
  
  public synchronized void V() {
    if(_czeka > 0) {
      this.notify();
    }
    _stan = true;
  }
}
  
class IThread extends Thread {
  private Counter _cnt;
  private Semafor semafor;
  public IThread(Counter c, Semafor semafor) {
    _cnt = c;
    this.semafor = semafor;
  }
  public void run() {
    for (int i = 0; i < 1000000; ++i) {
      try {
        this.semafor.P();
        _cnt.inc();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        this.semafor.V();
      }
    }
  }
}

class DThread extends Thread {
  private Counter _cnt;
  private Semafor semafor;
  public DThread(Counter c, Semafor semafor) {
    _cnt = c;
    this.semafor = semafor;
  }
  public void run() {
    for (int i = 0; i < 1000000; ++i) {
      try {
        this.semafor.P();
        _cnt.dec();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        this.semafor.V();
      }
    }
  }
}

class Zad1 {
  public static void main(String[] args) {
    Counter cnt = new Counter(0);
    Semafor semafor = new Semafor();
    IThread it = new IThread(cnt, semafor);
    DThread dt = new DThread(cnt, semafor);
    
    it.start();
    dt.start();
    
    try {
      it.join();
      dt.join();
    } catch (InterruptedException e) { }
    
    System.out.println("value=" + cnt.value());
  }
}
