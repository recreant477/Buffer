package consumer;

import buffer.Buffer;

import java.io.IOException;

public class Consumer implements Runnable {

    private static final String TEMPLATE = "%s %s";
    private final Buffer buffer;

    private boolean flag = true;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (flag) {
            synchronized (buffer) {
                if (!buffer.getList().isEmpty()) {
                    try {
                        System.out.println(String.format(TEMPLATE, buffer.get(), Thread.currentThread().getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    buffer.notifyAll();
                } else {
                    flag = false;
                }
            }
        }
    }
}
