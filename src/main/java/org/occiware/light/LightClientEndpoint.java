package org.occiware.light;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.websocket.*;

/**
 * This client endpoint manage query connection to light websocket server.
 * @author Christophe Gourdin - (c) Inria - 2016
 */
@ClientEndpoint
public class LightClientEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(LightClientEndpoint.class);
    
    private String response;
    private Throwable exception;
    private static final int REQUEST_TIMEOUT_SECS = 10;
    // on or off, represent the message to send to websocket server.
    private String msg;
    
    private final CountDownLatch messageLatch = new CountDownLatch(1);
    
    
    
    public LightClientEndpoint(final String msg) {
        this.msg = msg;
    }
    
    @OnOpen
    public void onOpen(Session session) {
        try {
            log.debug("Sending request: '" + msg + "' with session " + session.getId());
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            log.error("Unable to connect to websocket server: ", e);
        }
    }
    @OnMessage
    public void processResponse(Session session, String message) {
        log.debug("Received response: '" + message + "' for request: '" + msg + "' with session " + session.getId());
        response = message;
        messageLatch.countDown();
    }
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Communication error, command light to '" + msg + "' with session " + session.getId(), throwable);
        exception = throwable;
        messageLatch.countDown();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("Disconnected: " + closeReason);
    }
    
    
    /**
     * Blocks until either the server sends a response to the request, 
     * an communication error occurs or the communication request times out.
     *
     * @return the server response message.
     * @throws TimeoutException if the server does not respond before the timeout value is reached.
     * @throws InterruptedException if the communication thread is interrupted (e.g. thread.interrupt() is invoked on it for cancellation purposes).
     * @throws IOException if a communication error occurs.
     */
    public String getResponse() throws TimeoutException, InterruptedException, IOException {
        if (messageLatch.await(REQUEST_TIMEOUT_SECS, TimeUnit.SECONDS)) {
            if (exception != null) {
                throw new IOException("Unable to command light", exception);
            }
            return response;
        } else {
            throw new TimeoutException("Timed out awaiting server response for " + msg);
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
    
}
