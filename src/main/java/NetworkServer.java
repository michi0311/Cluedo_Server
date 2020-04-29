import java.io.IOException;
import dto.BaseMessage;
/****************************
 * Created by Michael Marolt *
 *****************************/

public interface NetworkServer {

    void start() throws IOException;

    void registerCallback(Callback<BaseMessage> callback);

    void broadcastMessage(BaseMessage message);
}
