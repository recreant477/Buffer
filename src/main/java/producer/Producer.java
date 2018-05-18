package producer;

import buffer.Buffer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Producer implements Runnable {

    private static final Calendar startTime = Calendar.getInstance();
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final int MAX_BUFFER = 5;

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
            produce();
            synchronized (buffer) {
                if (buffer.getList().size() < MAX_BUFFER) {
                    try {
                        buffer.put(new SimpleDateFormat(FORMAT).format(new Date()));
                    } catch (IOException e) {
                        System.out.println("Ошибка ввода вывода");
                    }
                    buffer.notifyAll();
                } else {
                    stopThread();
                }
            }
        }
    }

    private synchronized boolean isValidMaxTime() {
        long currentMillis = Calendar.getInstance().getTimeInMillis();
        long startMillis = startTime.getTimeInMillis();
        return endTimeInSeconds >= currentMillis - startMillis;
    }

    private void stopThread() {
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
