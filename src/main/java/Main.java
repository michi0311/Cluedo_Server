import dto.*;
import kryonet.Callback;
import kryonet.ServerKryonet;

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
        server.registerClass(RequestDTO.class,1);
        server.registerClass(RegisterClassDTO.class,2);
        server.registerClass(SerializedDTO.class,3);
        //server.registerClass(Class.class);
        //server.registerClass(Object.class);
        //server.registerClass(TextMessage.class);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.registerCallback(new Callback<RequestDTO>() {
            @Override
            public void callback(RequestDTO argument) {
                server.broadcastMessage(argument);
            }
        });
    }
}
