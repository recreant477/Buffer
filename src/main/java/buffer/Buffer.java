package buffer;

import java.io.*;
import java.nio.file.*;

public class Buffer {

    private static final Path FILE_ORIGINAL_PATH = Paths.get("./src/main/resources/buffer.txt");
    private static final Path FILE_COPY_PATH = Paths.get("./src/main/resources/bufferCopy.txt");
    private static final Path DIRECTORY_PATH = Paths.get("./src/main/resources");
    private static final int MAX_SIZE = 30;

    public static volatile int countConsumed = 0;
    public static volatile int countProducer = 0;

    private static volatile boolean lock = true;

    private boolean producerWorks = true;
    private File originalFileBuffer;

    public Buffer() {
        this.originalFileBuffer = this.readFileBuffer();
    }

    public synchronized <T> T get() {
        T result = null;
        if (!lock) {
            originalFileBuffer = readFileBuffer();
            File copyFile = FILE_COPY_PATH.toFile();
            try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(originalFileBuffer));
                 ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(copyFile))) {
                if ((result = (T) reader.readObject()) != null) {
                    ++countConsumed;
                    T newLine;
                    while ((newLine = (T) reader.readObject()) != null) {
                        writer.writeObject(newLine);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                //Не обрабатываем
            }
            originalFileBuffer.delete();
            copyFile.renameTo(originalFileBuffer);
            this.notifyAll();
            lock = true;
        }
        return result;
    }

    private synchronized void isMaxSizeBuffer() {
        if (MAX_SIZE <= originalFileBuffer.length()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized <T> void put(T el) {
        if (lock) {
            originalFileBuffer = readFileBuffer();
            isMaxSizeBuffer();
            try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(originalFileBuffer))) {
                writer.writeObject(el);
                writer.flush();
                ++countProducer;
            } catch (IOException ex) {
            }
            lock = false;
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
