package producer;

import buffer.Buffer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Producer implements Runnable {

    private static final Calendar startTime = Calendar.getInstance();
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final int MAX_SIZE = 30;

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
            try {
                synchronized (buffer) {
                    String timestamp = new SimpleDateFormat(FORMAT).format(new Date());
                    produce();
                    while (buffer.getOriginalFileBuffer().length() > MAX_SIZE) {
                        suspendWork();
                    }
                    if (buffer.isProducerWorks()) {
                        buffer.put(timestamp);
                    }
                    buffer.notifyAll();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        buffer.setProducerWorks(false);
    }

    private boolean isValidMaxTime() {
        long currentMillis = Calendar.getInstance().getTimeInMillis();
        long startMillis = startTime.getTimeInMillis();
        return endTimeInSeconds >= currentMillis - startMillis;
    }

    private void suspendWork() {
        try {
            buffer.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void produce() {
        try {
            Thread.sleep(operatingFrequency);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
