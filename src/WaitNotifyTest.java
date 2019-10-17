public class WaitNotifyTest {
    private int myInc = 0;
    private boolean finish = false;
    public boolean isPrint;
    public boolean isIncrement;
    private final int AMOUNT_OF_ITERATIONS = 1000000;

    public static void main(String[] args) {
        WaitNotifyTest waitNotifyTest = new WaitNotifyTest();
        Runnable inc = waitNotifyTest::incremenator;
        Thread increment = new Thread(inc);
        increment.start();

        Runnable incPrint = waitNotifyTest::printer;
        Thread incrementPrint = new Thread(incPrint);
        incrementPrint.start();
    }
    public synchronized void incremenator () {
        while (myInc < AMOUNT_OF_ITERATIONS) {
            myInc++;
            isIncrement = true;
            isPrint = false;
            while (!isPrint) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
            notify();

        }

        finish = true;
        notifyAll();
    }
    public synchronized void printer () {
        while (!finish) {
            System.out.println(myInc);
            isIncrement = false;
            isPrint = true;
            notify();
            while (!isIncrement) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }

        }


    }
}

