import Interfaces.Client;
import Interfaces.Connection;
import Interfaces.Server;

public class Main {
    public static void main (String[] args) {
        Server s = new Server ((client) -> new Connection (client) {
            @Override
            public void consume (String line) {
                this.write (line.toUpperCase ());
            }
        }, 8083);

        s.start ();

        new Client ("localhost", 8083) {

            @Override
            public void bobDoSomething () {
                this.write ("Le mie palle");
            }

            @Override
            public void consume (String line) {
                System.out.println (line);
            }
        };
    }
}
