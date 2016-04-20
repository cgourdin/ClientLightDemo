package org.occiware.light;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

/**
 * Client to connect to websocket server for light on off management.
 *
 * @author christophe
 */
public class LightClient {

    protected ClientManager client;
    protected Session session;

    /**
     * Create a new light on display.
     */
    public static final String CREATE_LIGHT = "create";
    /**
     * Remove a light from display and free resource.
     */
    public static final String DELETE_LIGHT = "delete";
    /**
     * Update light location.
     */
    public static final String UPDATE_LIGHT_LOCATION = "updateLocation";

    /**
     * Command switchOn, Turn light on.
     */
    public static final String SWITCH_ON = "switchOn";
    /**
     * Command switchOff, light is off.
     */
    public static final String SWITCH_OFF = "switchOff";

    /*
     * Server address endpoint.
     */
    private String serverAddress = "ws://localhost:8025/websocket/light";
    // TODO : Make a property file to read this.

    public LightClient() {
    }
    
    public LightClient(final String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Connect to server and query for msg.
     *
     * @param endpoint (warning endpoint must be instancied at each msg launch).
     * @return the response of the server.
     * @throws IOException
     * @throws TimeoutException
     */
    public String connectAndQuery(LightClientEndpoint endpoint) throws IOException, TimeoutException {
        String response = null;
        try {
            client = ClientManager.createClient();
            session = client.connectToServer(
                    endpoint,
                    URI.create(serverAddress)
            );

            response = endpoint.getResponse();
        } catch (DeploymentException e) {
            throw new IOException(e);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
        return response;
    }

    /**
     * Disconnect from server.
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (client != null && session != null) {
            session.close();
        }
    }

    /**
     * Send a message with autoconnect and autodisconnect.
     *
     * @param message
     * @return a response.
     * @throws IOException
     * @throws TimeoutException
     */
    public String sendMessage(final String message) throws IOException, TimeoutException {
        String response;
        try {
            LightClientEndpoint endpoint = new LightClientEndpoint(message);
            response = connectAndQuery(endpoint);

        } finally {
            this.disconnect();
        }
        return response;
    }
    
    
    /**
     * Create a light on display.
     * @param id
     * @param location
     * @return A response from server
     * @throws IOException 
     * @throws TimeoutException 
     */
    public String createLight(final String id, final String location) throws IOException, TimeoutException {
        return this.sendMessage(id + ";" + CREATE_LIGHT + ";" + location);
    }
    /**
     * Remove a light from display.
     * @param id
     * @return
     * @throws IOException
     * @throws TimeoutException 
     */
    public String deleteLight(final String id) throws IOException, TimeoutException {
        return this.sendMessage(id + ";" + DELETE_LIGHT);
    }
    /**
     * Update light location (ex: dining room).
     * @param id
     * @param location
     * @return
     * @throws IOException
     * @throws TimeoutException 
     */
    public String updateLightLocation(final String id, final String location) throws IOException, TimeoutException {
        return this.sendMessage(id + ";" + UPDATE_LIGHT_LOCATION + ";" + location);
    }
    /**
     * Turn light on.
     * @param id
     * @return "ok" if no error.
     * @throws IOException
     * @throws TimeoutException 
     */
    public String switchOn(final String id) throws IOException, TimeoutException {
        return this.sendMessage(id + ";" + SWITCH_ON);
    }
    /**
     * Turn light off.
     * @param id
     * @return "ok" if no error.
     * @throws IOException
     * @throws TimeoutException 
     */
    public String switchOff(final String id) throws IOException, TimeoutException {
        return this.sendMessage(id + ";" + SWITCH_OFF);
    }
    
}
