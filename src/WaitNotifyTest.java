import java.util.concurrent.*;

public class WaitNotifyTest {
    private int myInc = 0;
    private boolean finish = false;
    public boolean isPrint;
    public boolean isIncrement = false;
    private final int AMOUNT_OF_ITERATIONS = 1000000;

    public static void main(String[] args) {
        WaitNotifyTest waitNotifyTest = new WaitNotifyTest();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 1000; i++) {

            Runnable inc = waitNotifyTest::incremenator;
            //Thread increment = new Thread(inc);
            //increment.start();

            Runnable incPrint = waitNotifyTest::printer;
            //Thread incrementPrint = new Thread(incPrint);
            //incrementPrint.start();


            executor.submit(inc);
            executor.submit(incPrint);

            if (waitNotifyTest.myInc != waitNotifyTest.AMOUNT_OF_ITERATIONS) {
                System.out.println("ERROR" + waitNotifyTest.myInc);
            }
        }


        if (waitNotifyTest.myInc == waitNotifyTest.AMOUNT_OF_ITERATIONS) {
            try {
                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                executor.shutdownNow();
            }
        }
    }

    public synchronized void incremenator () {
        while (myInc < AMOUNT_OF_ITERATIONS && !Thread.currentThread().isInterrupted()) {
            myInc++;
            isIncrement = true;
            isPrint = false;
            notify();
            while (!isPrint) {
                //System.out.println(Thread.currentThread());
                try {
                    wait();
                } catch (InterruptedException e) {
                        break;

                }
            }


        }

        finish = true;
        notifyAll();
    }
    public synchronized void printer () {
        while (!finish && !Thread.currentThread().isInterrupted()) {
            System.out.println(myInc);
            isIncrement = false;
            isPrint = true;
            notify();
            while (!isIncrement) {
                //System.out.println(Thread.currentThread());
                try {
                    wait();
                } catch (InterruptedException e) {
                        break;
                }
            }

        }


    }
}

