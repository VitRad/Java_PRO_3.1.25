public class Main {
    public static void main(String[] args) throws InterruptedException {

        RadionovThreadPool rtp = new RadionovThreadPool(2);
        for (int i = 0; i < 10; i++) {
            rtp.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
        rtp.shutdown();
        rtp.awaitTermination();
        System.out.println("Все задачи выполнены");
    }
}