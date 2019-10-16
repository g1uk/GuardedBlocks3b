import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockTest {
    private int myInc = 0;
    private boolean finish = false;
    private final int AMOUNT_OF_ITERATIONS = 1000000;
    ReentrantLock locker = new ReentrantLock();
    Condition condition = locker.newCondition();

    public static void main(String[] args) {
            TryLockTest tryLockTest = new TryLockTest();
            Runnable inc = tryLockTest::incremenator;
            Runnable incPrint = tryLockTest::printer;

            Thread increment = new Thread(inc);
            increment.start();

            Thread incrementPrint = new Thread(incPrint);
            incrementPrint.start();



    }
    public void incremenator () {
        locker.lock();
        try {
        while (myInc < AMOUNT_OF_ITERATIONS) {
                myInc++;
                    condition.signal();
                    condition.await();


            }
        } catch (InterruptedException e) {

        } finally {

            locker.unlock();
        }
        finish = true;
        //condition.signalAll();
    }
    public void printer () {
            locker.lock();
        try {
            while (!finish) {
                System.out.println(myInc);
                condition.signal();
                condition.await();
                }

            } catch (InterruptedException e) {

        } finally {

            locker.unlock();
        }
    }
}


