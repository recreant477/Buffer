package producer;

import buffer.Buffer;

import java.util.Calendar;

public class Producer implements Runnable {

    private static final Calendar startTime = Calendar.getInstance();
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final int MAX_SIZE = 30;

    private static volatile int i = 0;
    private final Buffer buffer;

    private int operatingFrequency;
    private int endTimeInSeconds;

    public Producer(Buffer buffer, int operatingFrequency, int endTimeInSeconds) {
        this.buffer = buffer;
        this.operatingFrequency = operatingFrequency;
        this.endTimeInSeconds = endTimeInSeconds;
    }

    @Override
    public void run() {
        while (isValidMaxTime()) {
            String res = String.valueOf(System.currentTimeMillis());
            produce();
            System.out.println(res);
            buffer.put(res);
        }
        System.out.println("Вышел producer " + i++);
        buffer.setProducerWorks(false);
    }

    private boolean isValidMaxTime() {
        long currentMillis = Calendar.getInstance().getTimeInMillis();
        long startMillis = startTime.getTimeInMillis();
        return endTimeInSeconds >= currentMillis - startMillis;
    }

    private void produce() {
        try {
            Thread.sleep(operatingFrequency);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
