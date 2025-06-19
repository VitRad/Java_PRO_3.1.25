public class Main {
    public static void main(String[] args) {

        RadionovThreadPool rtp = new RadionovThreadPool(5);
        for (int i = 0; i < 10; i++) {
            rtp.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
        rtp.shutdown();
    }
}