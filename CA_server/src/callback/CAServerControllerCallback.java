package callback;

import java.util.concurrent.CountDownLatch;

public interface CAServerControllerCallback
{
    void addLogMessage(String logMessage);
    void clientConnected(String clientID, String connectTime, CountDownLatch latch);
}
