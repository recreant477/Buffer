package buffer;

import java.io.*;
import java.nio.file.*;

public class Buffer {

    private static final Path FILE_ORIGINAL_PATH = Paths.get("./src/main/resources/buffer.txt");
    private static final Path FILE_COPY_PATH = Paths.get("./src/main/resources/bufferCopy.txt");
    private static final Path DIRECTORY_PATH = Paths.get("./src/main/resources");
    private static final String TEMPLATE = "%s %s";

    public static volatile int countConsumed = 0;
    public static volatile int countProducer = 0;

    private volatile boolean producerWorks = true;

    private File originalFileBuffer;

    public Buffer() {
        try {
            this.originalFileBuffer = this.readFileBuffer();
        } catch (IOException e) {
            System.out.println("Ошибка ввода вывода");
        }
    }

    public synchronized String get() throws IOException {
        File original = this.readFileBuffer();
        File copy = FILE_COPY_PATH.toFile();
        String result = "";

        try (BufferedReader br = new BufferedReader(new FileReader(original));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(copy))) {
            if ((result = br.readLine()) != null) {
                String newLine;
                while ((newLine = br.readLine()) != null) {
                    bufferedWriter.write(String.valueOf(newLine) + "\n");
                }
                countConsumed++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        original.delete();
        copy.renameTo(original);

        return String.format(TEMPLATE, result, Thread.currentThread().getName());
    }

    public synchronized void put(String el) throws IOException {
        try (FileWriter writer = new FileWriter(this.readFileBuffer())) {
            writer.write(el);
            writer.write(el + "\n");
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        countProducer++;
    }

    private File readFileBuffer() throws IOException {
        createDirectory();
        return getFileBuffer();
    }

    private File getFileBuffer() throws IOException {
        if (!Files.exists(FILE_ORIGINAL_PATH, LinkOption.NOFOLLOW_LINKS)) {
            return Files.createFile(FILE_ORIGINAL_PATH).toFile();
        }
        return FILE_ORIGINAL_PATH.toFile();
    }

    private void createDirectory() throws IOException {
        if (!Files.exists(DIRECTORY_PATH, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectory(FileSystems.getDefault().getPath(DIRECTORY_PATH.toString()));
        }
    }

    public synchronized void removeBuffer() {
        try {
            if (Files.exists(FILE_ORIGINAL_PATH)) {
                Files.delete(FILE_ORIGINAL_PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized File getOriginalFileBuffer() {
        return originalFileBuffer;
    }

    public synchronized boolean isProducerWorks() {
        return producerWorks;
    }

    public synchronized void setProducerWorks(boolean producerWorks) {
        this.producerWorks = producerWorks;
    }
}
