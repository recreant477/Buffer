package buffer;

import java.io.*;
import java.nio.file.*;

public class Buffer {

    private static final Path FILE_ORIGINAL_PATH = Paths.get("./src/main/resources/buffer.txt");
    private static final Path FILE_COPY_PATH = Paths.get("./src/main/resources/bufferCopy.txt");
    private static final Path DIRECTORY_PATH = Paths.get("./src/main/resources");

    public static volatile int countConsumed = 0;
    public static volatile int countProducer = 0;

    private static volatile boolean lock = true;
    private static volatile boolean producerWorks = true;

    private File originalFileBuffer;

    public Buffer() {
        this.originalFileBuffer = this.readFileBuffer();
    }

    public synchronized String get() throws IOException {
        String result = null;
        if (!lock) {
            originalFileBuffer = readFileBuffer();
            if (originalFileBuffer.length() != 0) {
                File copy = FILE_COPY_PATH.toFile();
                BufferedReader br = new BufferedReader(new FileReader(originalFileBuffer));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(copy));

                if ((result = br.readLine()) != null) {
                    String newLine;
                    while ((newLine = br.readLine()) != null) {
                        bufferedWriter.write(String.valueOf(newLine) + "\n");
                    }
                    br.close();
                    bufferedWriter.close();

                    originalFileBuffer.delete();
                    copy.renameTo(originalFileBuffer);
                    lock = true;
                    countConsumed++;
                }
            }
        }

        return result;
    }

    public synchronized void put(String el) {
        if (lock) {
            originalFileBuffer = readFileBuffer();
            try (FileWriter writer = new FileWriter(originalFileBuffer)) {
                writer.write(el);
                writer.write(el + "\n");
                writer.flush();

                countProducer++;
                lock = false;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private synchronized File readFileBuffer() {
        try {
            createDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getFileBuffer();
    }

    private synchronized File getFileBuffer() {
        if (!Files.exists(FILE_ORIGINAL_PATH)) {
            try {
                return Files.createFile(FILE_ORIGINAL_PATH).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FILE_ORIGINAL_PATH.toFile();
    }

    private synchronized void createDirectory() throws IOException {
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
