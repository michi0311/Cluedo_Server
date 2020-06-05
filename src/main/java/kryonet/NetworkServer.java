package kryonet;

import java.io.IOException;
import dto.BaseMessage;
import dto.RequestDTO;
import kryonet.Callback;

/****************************
 * Created by Michael Marolt *
 *****************************/

public interface NetworkServer {

    void start() throws IOException;

    void registerCallback(Callback<RequestDTO> callback);

    void broadcastMessage(RequestDTO message);
}
