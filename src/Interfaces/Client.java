package Interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client {
    private final PrintWriter writer;
    private final BufferedReader reader;
    private boolean running = true;

    public Client (String host, int port) {
        PrintWriter writerDummy;
        BufferedReader readerDummy;
        try {
            Socket server = new Socket (host, port);
            writerDummy = new PrintWriter (server.getOutputStream (), true);
            readerDummy = new BufferedReader (new InputStreamReader (server.getInputStream ()));
        } catch (IOException e) {
            writerDummy = null;
            readerDummy = null;
        }

        this.writer = writerDummy;
        this.reader = readerDummy;

        this.run ();
    }

    public void stopRunning () {
        this.running = false;
    }

    public void write (String... lines) {
        for (String line : lines) this.writer.println (line);
    }

    public void run () {
        new Thread (() -> {
            while (this.running) {
                try {
                    this.bobDoSomething ();
                    this.consume (this.reader.readLine ());
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }).start ();
    }

    public abstract void bobDoSomething (Object... parameters);

    public abstract void consume (String line);
}
