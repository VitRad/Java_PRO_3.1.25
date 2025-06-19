import java.util.LinkedList;

public class RadionovThreadPool {
    private final LinkedList<Runnable> queue = new LinkedList<>();
    private final Thread[] threads;
    private volatile boolean isShutdown = false;

    public RadionovThreadPool(int pollSize) {
        this.threads = new Thread[pollSize];
        for (int i = 0; i < pollSize; i++) {
            int cnt = i;
            threads[i] = new Thread(() -> {
                System.out.println("Start thread: " + cnt);
            });
            threads[i].start();
        }
    }
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("Пул потоков остановлен");
        }
        queue.add(task);
    }
    public void shutdown() {
        isShutdown = true;
        for (Thread workerThread : threads) {
            workerThread.interrupt();
        }
    }
}