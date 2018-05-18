package buffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Buffer {

    private static final Path PATH = Paths.get("./src/main/resources/buffer.txt");

    private List<String> list = new ArrayList<>();
    private boolean flag = true;

    public Buffer() {
        try {
            this.list = this.readFileBuffer();
        } catch (IOException e) {
            System.out.println("Ошибка ввода вывода");
        }
    }

    public synchronized String get() throws IOException {
        while (!flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String result = "";
        if (list != null && !list.isEmpty()) {
            list = readFileBuffer();
            result = list.get(0);
            list.remove(0);
            writeToFileBuffer();
        }
        flag = true;
        return result;
    }

    public synchronized void put(String el) throws IOException {
        while (flag){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list = readFileBuffer();
        list.add(el);
        writeToFileBuffer();
        flag = false;
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

    public List<String> getList() {
        return list;
    }

    public boolean isFlag() {
        return flag;
    }
}
