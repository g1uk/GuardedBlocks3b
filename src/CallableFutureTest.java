import java.util.concurrent.*;

public class CallableFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int count = 0;

        for (int i = 0; i < 1000; i++) {
            Callable taskOne = () -> {
                Thread.sleep(10000);
                return 50;
            };
            Callable taskTwo = () -> {
                Thread.sleep(5000);
                return 100;
            };
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            Future<Integer> numberFromTaskOne = executor.submit(taskOne);
            Future<Integer> numberFromTaskTwo = executor.submit(taskTwo);

            Integer productOfNumbers = numberFromTaskOne.get() * numberFromTaskTwo.get();
            //System.out.println(result);
            if (numberFromTaskOne.isDone() && numberFromTaskTwo.isDone()) {
                try {
                    executor.shutdown();
                    executor.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (!executor.isTerminated())
                        executor.shutdownNow();
                }
            }

            count++;
            System.out.println(count);
            if (productOfNumbers != 5000) {
                System.out.println("ERROR" + productOfNumbers);
            }
        }



    }

}
