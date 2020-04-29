import dto.BaseMessage;
import dto.TextMessage;

import java.io.IOException;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class Main {
    public static void main(String[] args) {
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
                System.out.println(argument.toString());
            }
        });
    }
}
