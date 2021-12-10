package files;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        UserRecorder start = new UserRecorder();
        start.run();
    }
}