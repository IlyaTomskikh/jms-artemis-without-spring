import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Scanner;

public class MessageDispatcher extends ConnectionHandler {
    public static void send() {
        var scanner = new Scanner(System.in);
        System.out.println("\n\nSEND THIS:");
        try {
            String str = scanner.nextLine();
            TextMessage message = session.createTextMessage(str);
            producer.send(message);
            System.out.println(
                    "\nThread ID = " + Thread.currentThread().getId() +
                    "\nSent: " + message.getText() +
                    "\nFree memory in heap: " + Runtime.getRuntime().freeMemory() + " bytes." +
                    "\n_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n");
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            if (onException()) {
                System.out.println("After 'onException': " + Runtime.getRuntime().freeMemory() + " bytes.");
                send();
            }
        }
    }
}
