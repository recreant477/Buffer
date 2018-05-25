import initialization.ProcessThread;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ProcessThread processThread = new ProcessThread();
        processThread.initProcess();
  /*      Buf buf = new Buf();
        Thread thread1 = new Thread(new P(buf, 1000));
        Thread thread2 = new Thread(new P(buf, 1000));
        Thread thread3 = new Thread(new P(buf, 1000));
        Thread thread4 = new Thread(new C(buf));
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();*/
    }
/*
    static class C implements Runnable {
        Buf buf;

        public C(Buf buf) {
            this.buf = buf;
        }

        @Override
        public void run() {
            while (buf.isProducerWorks() || !buf.getOriginalFileBuffer().isEmpty()) {
                try {
                    String res = buf.get();
                    if (res != null) {
                        System.out.println(res);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class P implements Runnable {
        Buf buf;
        private int endTimeInSeconds;
        private static final Calendar startTime = Calendar.getInstance();

        public P(Buf buf, int endTimeInSeconds) {
            this.buf = buf;
            this.endTimeInSeconds = endTimeInSeconds;
        }

        @Override
        public void run() {
            while (isValidMaxTime()) {
                try {
                    String res = String.valueOf(System.currentTimeMillis());
                    buf.put(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            buf.setProducerWorks(false);
        }

        private boolean isValidMaxTime() {
            long currentMillis = Calendar.getInstance().getTimeInMillis();
            long startMillis = startTime.getTimeInMillis();
            return endTimeInSeconds >= currentMillis - startMillis;
        }
    }

    static class Buf {

        private List<String> list = new ArrayList<>();
        private Object lock = new Object();
        boolean producerWorks = true;

        public String get() throws IOException {
            String res = null;
            synchronized (lock) {
                if (list.size() > 0) {
                    res = list.get(0);
                    list.remove(0);
                }
                lock.notifyAll();
            }
            return res;
        }

        public void put(String el) throws IOException {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (list.size() > 5) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            synchronized (lock) {
                list.add(el);
                lock.notifyAll();
            }
        }

        public synchronized boolean isProducerWorks() {
            return producerWorks;
        }

        public synchronized void setProducerWorks(boolean producerWorks) {
            synchronized (lock){
                lock.notifyAll();
                this.producerWorks = producerWorks;
            }
        }
        public synchronized List getOriginalFileBuffer() {
            return list;
        }

    }*/
}
