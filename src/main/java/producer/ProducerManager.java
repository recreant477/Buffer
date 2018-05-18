package producer;

import buffer.Buffer;

import java.util.Scanner;

public class ProducerManager {

    private static final String COUNT = "Задайте количество генерируещих (producer) : ";
    private static final String FREQUENCY = "Задайте частоту работы (producer)) : ";
    private static final String TIME = "Задайте время работы (producer) в миллисекундах) : ";

    private int endTimeInSeconds;
    private int sizeProducers;
    private int operatingFrequency;

    private final Buffer buffer;

    public ProducerManager(Buffer buffer) {
        this.buffer = buffer;
    }

    public void startProcess() {
        sizeProducers = getData(COUNT);
        operatingFrequency = getData(FREQUENCY);
        endTimeInSeconds = getData(TIME);

        startProducers();
    }

    private int getData(String template) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(template);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }
        return 0;
    }

    private void startProducers() {
        for (int i = 0; i < sizeProducers; i++) {
            new Thread(new Producer(buffer, operatingFrequency, endTimeInSeconds)).start();
        }
        stopProducer();
    }

    private void stopProducer() {
        try {
            Thread.sleep(endTimeInSeconds);
            synchronized (buffer) {
                buffer.notifyAll();
            }
        } catch (InterruptedException e) {
            System.out.println("Ошибка ввода вывода");
        }
    }
}

