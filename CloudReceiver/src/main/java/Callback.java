import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Callback implements MqttCallback {
    String message;

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to broker lost.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        message= mqttMessage.toString();
        message = message.replaceAll("\\[", "").replaceAll("\\]","");
        Main.fileWriter(message);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
