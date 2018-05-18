package consumer;

import buffer.Buffer;
import producer.Producer;

import java.util.Scanner;

public class ConsumerManager {

        private static final String COUNT = "Задайте количество генерируещих (producer) : ";
        private static final String FREQUENCY = "Задайте частоту работы (producer)) : ";
        private static final String TIME = "Задайте время работы (producer) в секундах) : ";

        private int endTimeInSeconds;
        private int sizeProducers;
        private int operatingFrequency;

        private final Buffer buffer = new Buffer();

        public void startProcess() {
            sizeProducers = getData(COUNT);
            operatingFrequency = getData(FREQUENCY);
            endTimeInSeconds = getData(TIME);

        }

        private int getData(String template) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(template);
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
            return 0;
        }
}
