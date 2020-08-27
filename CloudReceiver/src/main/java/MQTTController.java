import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTController {
    private String broker;
    private String topic;
    private String clientID;
    private int qos;
    private MqttAsyncClient client;
    private String incomingMessage;

    //initialises variables
    public MQTTController(String broker, String topic, String clientID, int qos) throws MqttException {
        this.setClientID(clientID);
        this.setBroker(broker);
        this.setQos(qos);
        this.setTopic(topic);
        this.createClient();
    }

    //creates subscribe client
    public void createClient() throws MqttException{
        client = new MqttAsyncClient(this.getBroker(), this.getClientID());
        Callback callback = new Callback();
        client.setCallback(callback);
        IMqttToken token = client.connect();
        token.waitForCompletion();
        System.out.println("Connected");
        client.subscribe(this.getTopic(), this.getQos());
        System.out.println("Subscribed to " + this.getTopic());

    }


    //getters and setters
    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public MqttAsyncClient getClient() {
        return client;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(String incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

}
