package producer;

import buffer.Buffer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Producer implements Runnable {

    private static final Calendar startTime = Calendar.getInstance();
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

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
            String res = new SimpleDateFormat(FORMAT).format(new Date());
            produce();
            buffer.put(res);
        }
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
