import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        ConnectionHandler.run();
        var n = new Scanner(System.in).nextInt();
        for(var i = 0; i < n; ++i) {
            MessageDispatcher.send();
            MessageReceiver.receive();
        }
        ConnectionHandler.stop();
    }
}
