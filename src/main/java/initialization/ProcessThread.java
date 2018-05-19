package initialization;

import buffer.Buffer;
import managers.ConsumerManager;
import managers.ProducerManager;

import java.util.Calendar;

public class ProcessThread {

    private static final String PRODUCED = "Произвели : %s";
    private static final String CONSUMED = "Потребили : %s";
    private static final String TIME_WORKS = "Время работы : %s миллисекунд";
    private static final String END_TIME = "Времени осталось : %s миллисекунд";

    private final Buffer buffer = new Buffer();
    private final ProducerManager producerThread = new ProducerManager(buffer);
    private final ConsumerManager consumerThread = new ConsumerManager(buffer);

    public void initProcess() {
        Calendar startTime = Calendar.getInstance();
        producerThread.start();
        consumerThread.start();

        getStatistics(startTime);
    }

    private void getStatistics(Calendar startTime) {
        while (buffer.isProducerWorks()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long diff = getTimeDifference(startTime);
            System.out.println(String.format(PRODUCED, Buffer.countProducer));
            System.out.println(String.format(CONSUMED, Buffer.countConsumed));
            System.out.println(String.format(TIME_WORKS, diff));
            System.out.println(String.format(END_TIME, getEndTime(diff)));
        }
    }

    private long getTimeDifference(Calendar startTime) {
        Calendar currentDate = Calendar.getInstance();
        return currentDate.getTimeInMillis() - startTime.getTimeInMillis();
    }

    private long getEndTime(long diff) {
        long endTime = producerThread.getEndTimeInSeconds() - diff;
        if (endTime < 0) {
            return 0;
        }
        return endTime;
    }
}
