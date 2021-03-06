package managers;

import buffer.Buffer;
import consumer.Consumer;

import java.util.Scanner;

public class ConsumerManager extends Thread {

    private static final String COUNT_CONSUMER = "Задайте количество потребляющих(consumer) : ";

    private final Buffer buffer;

    private int sizeConsumer;

    public ConsumerManager(Buffer buffer) {
        this.buffer = buffer;
        sizeConsumer = getSizeConsumer();
    }

    @Override
    public void run() {
        startConsumer();
    }

    private int getSizeConsumer() {
        int size = getData(COUNT_CONSUMER);
        if (size <= 0) {
            System.out.println("Задайте хотя бы один поток");
            size = getSizeConsumer();
        }
        return size;
    }

    private int getData(String template) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(template);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }
        return 0;
    }

    private void startConsumer() {
        for (int i = 0; i < sizeConsumer; i++) {
            new Thread(new Consumer(buffer)).start();
        }
    }
}
