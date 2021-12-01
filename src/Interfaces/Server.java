package Interfaces;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class Server {
    private final Function<Socket, Connection> connectionSupplier;

    // port where the server is hosted
    private final int port;

    // if debug is true, a debug messages in console will appear
    private boolean debug = true;

    public Server (Function<Socket, Connection> connectionSupplier, int port) {
        this.connectionSupplier = connectionSupplier;
        this.port = port;
    }

    public void debug (boolean debug) {
        this.debug = debug;
    }

    public void start () {
        /*
        * I don't know if when this class is used there will be other code that needs to be executed,
        * so I make sure that the blocking read actions are performed on a separate thread.
        */
        new Thread ( () -> {
            try {
                ServerSocket server = new ServerSocket (this.port);
                if (debug) System.out.println ("Started listening on port " + this.port);

                /*
                * keep listening for a new client over and over
                * when found, a new connection is created
                */
                while (true) {
                    Socket client = server.accept ();
                    if (debug) System.out.println ("Connected to a new client");

                    new Thread (this.connectionSupplier.apply (client)).start ();
                }
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }).start ();
    }
}
