import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String args[]) throws MqttException, IOException {

        // the broker string needs the ip and port of the broker in the format "tcp://xxx.xxx.xxx.xxx:pppp"
        // where x is the ip and p is the port
        String broker = "";

        String topic = "clus";
        String clientID = "cloud";
        int qos = 2;


        MQTTController controller = new MQTTController(broker, topic, clientID, qos);
    }

    public static void fileWriter(String message) throws IOException {
        String[] splitClusters = message.split("/");
        FileWriter writer = new FileWriter("test.csv", true);
        System.out.println(splitClusters[0]);
        String[] splitMessage = {};
        for(int x = 0; x < splitClusters.length; x++){
            splitMessage = splitClusters[x].split(", ");
            for (int i = 1; i < splitMessage.length; i++) {
                writer.append(splitMessage[i]);
                writer.append(",");
                writer.append(splitMessage[0]);
                writer.append("\n");
            }
        }

        writer.flush();
        writer.close();

    }

}
