import java.util.Scanner;

public class test3 implements Runnable {

    private volatile boolean shutdown = false;

    public static void main(String[] args) {

        test3 w = new test3();

        new Thread(w).start();

        System.out.println("Press any key to interrupt");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();

        w.triggerShutDown();
    }

    @Override
    public void run() {
            synchronized (this) {
                try {
                    System.out.println("doing some silly things");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        System.out.println("Server shutdown successfully");
    }

    public synchronized void triggerShutDown() {
        this.shutdown = true;
        notify();
    }
}