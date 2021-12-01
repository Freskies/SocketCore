package Interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Connection implements Runnable {
    private final PrintWriter writer;
    private final BufferedReader reader;
    private boolean running = true;

    public Connection (Socket client) {
        PrintWriter writerDummy;
        BufferedReader readerDummy;
        try {
            writerDummy = new PrintWriter (client.getOutputStream (), true);
            readerDummy = new BufferedReader (new InputStreamReader (client.getInputStream ()));
        } catch (IOException e) {
            writerDummy = null;
            readerDummy = null;
        }

        this.writer = writerDummy;
        this.reader = readerDummy;
    }

    public void stopRunning () {
        this.running = false;
    }

    public void write (String... lines) {
        for (String line : lines) this.writer.println (line);
    }

    @Override
    public void run () {
        while (this.running)
            try {
                this.consume (reader.readLine ());
            } catch (IOException e) {
                this.running = false;
            }
    }

    public abstract void consume (String line);
}
