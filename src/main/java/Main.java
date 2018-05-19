import initialization.ProcessThread;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ProcessThread processThread = new ProcessThread();
        processThread.initProcess();
    }
}
