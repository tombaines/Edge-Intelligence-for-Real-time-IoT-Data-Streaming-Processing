import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.FileNotFoundException;


public class MQTTController {
    //Initialise Variables
    private int QoS;
    private String topic;
    private String clientID;
    private String broker;
    private MqttClient client;

    //initializes values for object
    public MQTTController(String csvPath, String broker, String topic, String clientID, int qos) throws MqttException {
        this.setQoS(qos);
        this.setTopic(topic);
        this.setBroker(broker);
        this.setClientID(clientID);
        this.createClient();
    }
    //sets up client
    public void createClient() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(this.getBroker(), this.getClientID(), persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker: "+broker);
        client.connect(connOpts);
        System.out.println("Connected");
    }

    //publishes messages to MQTT broker
    public void sendMessage(String content, MqttClient client) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(this.getQoS());
        client.publish(this.getTopic(), message);
        System.out.println("Message published: " + content);
    }

    //disconnects mqtt broker
    public void disconnectClient(MqttClient client) throws MqttException {
        client.disconnect();
        System.out.println("Client Disconnected");
    }

    //Getters + Setters
    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public MqttClient getClient() {
        return client;
    }

    public int getQoS() {
        return QoS;
    }

    public String getTopic() {
        return topic;
    }

    public void setQoS(int qoS) {
        QoS = qoS;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
