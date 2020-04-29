import dto.BaseMessage;
import dto.TextMessage;

import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class Main {
    public static void main(String[] args) {
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);

        } catch (UnknownHostException e) {

            e.printStackTrace();
        }


        ServerKryonet server = new ServerKryonet();
        server.registerClass(BaseMessage.class);
        server.registerClass(TextMessage.class);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.registerCallback(new Callback<BaseMessage>() {
            @Override
            public void callback(BaseMessage argument) {
                server.broadcastMessage(argument);
            }
        });
    }
}
