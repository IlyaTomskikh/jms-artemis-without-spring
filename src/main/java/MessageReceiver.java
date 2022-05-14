import javax.jms.JMSException;
import javax.jms.TextMessage;

public class MessageReceiver extends ConnectionHandler {
    public static void receive() {
        TextMessage message = null;
        try {
            message = (TextMessage) messageConsumer.receive(1000);
        } catch (JMSException e) {
            System.out.println("Exceeded waiting time.");
            if (onException()) receive();
            else return;
        }
        try {
            ++num;
            System.out.println(
                    "Thread ID = " + Thread.currentThread().getId() +
                    "\nReceived: " + (message != null ? message.getText() : "null_msg") + " - msg number " + num +
                    "\nFree memory in heap: " + Runtime.getRuntime().freeMemory() + " bytes." +
                    "\n________________________________________________________________________\n");
        } catch (JMSException e) {
            System.out.println(e.getMessage());
        }
    }
}
