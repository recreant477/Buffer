import buffer.Buffer;
import consumer.Consumer;
import producer.ProducerManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Buffer buffer = new Buffer();
        ProducerManager producerManager = new ProducerManager(buffer);
        producerManager.startProcess();

        new Thread(new Consumer(buffer)).start();
        new Thread(new Consumer(buffer)).start();
        new Thread(new Consumer(buffer)).start();
      /*  new Thread(new Consumer(buffer)).start();
        new Thread(new Consumer(buffer)).start();
        new Thread(new Consumer(buffer)).start();
*/
    }
}
