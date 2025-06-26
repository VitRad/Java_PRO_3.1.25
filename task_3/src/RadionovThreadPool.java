import java.util.LinkedList;

public class RadionovThreadPool {
    private final LinkedList<Runnable> queue;
    private final Thread[] threads;
    private volatile boolean isShutdown;

    public RadionovThreadPool(int pollSize) {
        this.queue = new LinkedList<>();
        this.threads = new Thread[pollSize];
        this.isShutdown = false;
        for (int i = 0; i < pollSize; i++) {
            TaskProcessor tp = new TaskProcessor("Thread # " + i);
            tp.start();
            threads[i] = tp;
        }
    }

    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("Пул потоков остановлен");
        }
        System.out.println("Выполнение бизнес-логики приложения");
        queue.add(task);
    }

    public synchronized void shutdown() {
        isShutdown = true;
        synchronized (queue) {
            queue.notifyAll();
        }
    }

    public void awaitTermination() throws InterruptedException {
        for (Thread tp : threads) {
            tp.join();
        }
    }

    private class TaskProcessor extends Thread {
        public TaskProcessor(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            Runnable task;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty() && !isShutdown) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (isShutdown && queue.isEmpty()) {
                        return;
                    }

                    task = queue.poll();
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.err.println("Ошибка выполнения задания: " + e.getMessage());
                }
            }
        }
    }
}