package buffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Buffer {

    public static volatile int countConsumed = 0;
    public static volatile int countProducer = 0;

    private static final Path PATH = Paths.get("./src/main/resources/buffer.txt");
    private static final String TEMPLATE = "%s %s";

    private volatile boolean producerWorks = true;

    private List<String> list = new ArrayList<>();

    public Buffer() {
        try {
            this.list = this.readFileBuffer();
        } catch (IOException e) {
            System.out.println("Ошибка ввода вывода");
        }
    }

    public synchronized String get() throws IOException {
        String result = "";
        list = readFileBuffer();
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
            list.remove(0);
            writeToFileBuffer();
            countConsumed++;
        }
        return String.format(TEMPLATE, result, Thread.currentThread().getName());
    }

    public synchronized void put(String el) throws IOException {
        list = readFileBuffer();
        list.add(el);
        writeToFileBuffer();
        countProducer++;
    }

    private void writeToFileBuffer() throws IOException {
        if (Files.exists(PATH)) {
            Files.write(PATH, list, StandardCharsets.UTF_8);
        }
    }

    private List<String> readFileBuffer() throws IOException {
        if (Files.exists(PATH)) {
            return Files.readAllLines(PATH, StandardCharsets.UTF_8);
        } else {
            return Files.readAllLines(Files.createFile(PATH));
        }
    }

    public synchronized void removeBuffer() {
        try {
            if (Files.exists(PATH)) {
                Files.delete(PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<String> getList() {
        return list;
    }

    public synchronized boolean isProducerWorks() {
        return producerWorks;
    }

    public synchronized void setProducerWorks(boolean producerWorks) {
        this.producerWorks = producerWorks;
    }
}
