package consumer;

import buffer.Buffer;

import java.io.IOException;

public class Consumer implements Runnable {

    private final Buffer buffer;
    private static final String TEMPLATE = "%s %s";

    private static volatile int  i = 0;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.isProducerWorks() || buffer.getOriginalFileBuffer().length() != 0) {
            try {
                String res = buffer.get();
                if (res != null) {
                    System.out.println(String.format(TEMPLATE, res, Thread.currentThread().getName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Вышел потреб " + i++);
        buffer.removeBuffer();
    }
}
