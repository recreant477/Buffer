package consumer;

import buffer.Buffer;

import java.io.IOException;

public class Consumer implements Runnable {

    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.isProducerWorks() || !buffer.getList().isEmpty()) {
            try {
                synchronized (buffer) {
                    if (!buffer.getList().isEmpty()) {
                        System.out.println(buffer.get());
                    }
                    buffer.notifyAll();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        buffer.removeBuffer();
    }
}
