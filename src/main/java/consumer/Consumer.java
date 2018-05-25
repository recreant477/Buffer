package consumer;

import buffer.Buffer;

public class Consumer implements Runnable {

    private final Buffer buffer;
    private static final String TEMPLATE = "%s %s";

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.isProducerWorks() || buffer.getOriginalFileBuffer().length() >= 4) {
            Object res = buffer.get();
            if (res != null) {
                System.out.println(String.format(TEMPLATE, res, Thread.currentThread().getName()));
            }
        }
    }
}
