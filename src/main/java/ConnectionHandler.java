import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class ConnectionHandler {
    public static boolean isConnected = false;

    protected static MessageConsumer messageConsumer;
    protected static Session session;
    protected static MessageProducer producer;
    protected static int num = 0;
    //protected static Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    private static Connection connection;
    private static boolean connect() {
        try {
            Properties props = new Properties();
            props.put("java.naming.factory.initial", "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory");
            props.put("connectionFactory.ConnectionFactory", "tcp://localhost:61616");
            props.put("queue.queue/exampleQueue", "exampleQueue");
            var initialContext = new InitialContext(props);
            var queue = (Queue) initialContext.lookup("queue/exampleQueue");
            var factory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageConsumer = session.createConsumer(queue);
            producer = session.createProducer(queue);
            System.out.println("Connection to the ArtemisMQ has been established.");
        } catch (NamingException | JMSException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public static boolean run() {
        isConnected = connect();
        if (isConnected) return true;
        System.out.println("Couldn't connect to the JMS Artemis server.");
        return false;
    }
    public static void stop() {
        try {
            producer.close();
            messageConsumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            System.out.println(e.getMessage());
        }
        isConnected = false;
    }
    protected static boolean onException() {
        try {
            System.out.println("Before 'Thread::sleep(long l)': " + Runtime.getRuntime().freeMemory() + " bytes.");
            Thread.sleep(10000);
            System.out.println("After 'Thread::sleep(long l)': " + Runtime.getRuntime().freeMemory() + " bytes.");
            System.out.println("Before closing: " + Runtime.getRuntime().freeMemory() + " bytes.");
            stop();
            System.out.println("After closing: " + Runtime.getRuntime().freeMemory() + " bytes.");
            return run();
        } catch (InterruptedException e) {
            System.out.println("The server is shut down.");
            return false;
        }
    }
}
