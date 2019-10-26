package pl.edu.agh.lab2.zad3;

class Counter {
  private int _val;
  Counter(int n) {
    _val = n;
  }
  void inc() {
    _val++;
  }
  void dec() {
    _val--;
  }
  int value() {
    return _val;
  }
}

class Semafor {
  private boolean _stan = true;
  private int _czeka = 0;

  Semafor(boolean _stan) {
    this._stan = _stan;
    this._czeka = 0;
  }

  synchronized void P() {
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

  synchronized void V() {
    if(_czeka > 0) {
      this.notify();
    }
    _stan = true;
  }
}

class Semaphore {
  private int _size = 0;
  private Semafor x = new Semafor(true);
  private Semafor y = new Semafor(false);
  Semaphore(int size) {
    _size = size;
  }

  void P() {
    x.P();
    _size--;
    if(_size < 0 ) {
      x.V();
      y.P();
    } else {
      x.V();
    }
  }

  void V() {
    x.P();
    _size++;
    if(_size <= 0) {
      y.V();
      x.V();
    } else {
      x.V();
    }
  }
}

class IThread extends Thread {
  private Counter _cnt;
  private Semaphore semafor;
  IThread(Counter c, Semaphore semafor) {
    _cnt = c;
    this.semafor = semafor;
  }
  public void run() {
    for (int i = 0; i < 1000000; ++i) {
      this.semafor.P();
      _cnt.inc();
      this.semafor.V();
    }
  }
}

class DThread extends Thread {
  private Counter _cnt;
  private Semaphore semafor;
  DThread(Counter c, Semaphore semafor) {
    _cnt = c;
    this.semafor = semafor;
  }
  public void run() {
    for (int i = 0; i < 1000000; ++i) {
      this.semafor.P();
      _cnt.dec();
      this.semafor.V();
    }
  }
}

class Zad3 {
  public static void main(String[] args) {
    Counter cnt = new Counter(0);
    Semaphore semafor = new Semaphore(1);
    IThread it = new IThread(cnt, semafor);
    DThread dt = new DThread(cnt, semafor);
    
    it.start();
    dt.start();
    
    try {
      it.join();
      dt.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    System.out.println("value=" + cnt.value());
  }
}
