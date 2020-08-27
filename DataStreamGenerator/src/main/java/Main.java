import org.eclipse.paho.client.mqttv3.MqttException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//b8:27:eb:bf:9d:51
//00:0f:00:70:91:0a
//1c:bf:ce:15:ec:4d

public class Main {
    public static void main(String args[]) throws IOException, MqttException, InterruptedException {
        //MQTT Variables

        //insert the path to the dataset below
        String csvPath = "iot_telemetry_data.csv";

        // the broker string needs the ip and port of the broker in the format "tcp://xxx.xxx.xxx.xxx:pppp"
        // where x is the ip and p is the port
        String broker = "";

        String topic = "data";
        String clientID = "IoTSensor1";
        String content = "";
        int qos = 2;
        int lineNumber = 100000;
        String row = "";

        //Constructors
        BufferedReader csvReader = new BufferedReader(new FileReader(csvPath));
        MQTTController controller = new MQTTController(csvPath, broker, topic, clientID, qos);

        //Reads each line of the CSV file and converts it to MQTT message.
        while ((row = csvReader.readLine()) != null) {
            if (row.contains("1c:bf:ce:15:ec:4d")){
                String[] data = row.split(",");
                content = lineNumber + "," + data[8].replaceAll("^\"|\"$", "");
                controller.sendMessage(content, controller.getClient() );
                lineNumber++;
                TimeUnit.MILLISECONDS.sleep(5);
            }
        }
        controller.disconnectClient(controller.getClient());
        System.exit(0);
    }



}
