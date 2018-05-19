package managers;

import buffer.Buffer;
import producer.Producer;

import java.util.Scanner;

public class ProducerManager extends Thread {

    private static final String COUNT_PRODUCER = "Задайте количество генерируещих (producer) : ";
    private static final String FREQUENCY = "Задайте частоту работы (producer)) : ";
    private static final String TIME = "Задайте время работы (producer) в миллисекундах) : ";

    private int endTimeInSeconds;
    private int sizeProducers;
    private int operatingFrequency;

    private final Buffer buffer;

    public ProducerManager(Buffer buffer) {
        this.buffer = buffer;
        sizeProducers = getData(COUNT_PRODUCER);
        operatingFrequency = getData(FREQUENCY);
        endTimeInSeconds = getData(TIME);
    }

    @Override
    public void run() {
        startProducers();
    }

    public int getEndTimeInSeconds() {
        return endTimeInSeconds;
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
    }
}

