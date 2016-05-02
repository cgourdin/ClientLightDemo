package org.occiware.light;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Christophe Gourdin - (c) Inria 2016
 */
public class ClientTestMain {

    private static final Logger log = LoggerFactory.getLogger(ClientTestMain.class);

    public static void main(String[] args) {

        log.info("Starting...");

        // Create Three lights to test connection and create action command.
        log.info("Connecting to websocketserver...");
        LightClient lightClient = new LightClient();
        String id1 = "e00cfdb8-0a74-488c-ae2e-234068337948";
        String location1 = "Living room light";
        executeActionLight(lightClient, id1, LightClient.CREATE_LIGHT, location1);
        
        String id2 = "a4d58c55-9e73-41bc-834f-1fad497d6738";
        String location2 = "Dining room";
        executeActionLight(lightClient, id2, LightClient.CREATE_LIGHT, location2);
        
        String id3 = "7f7f8257-6e88-4ce9-be78-b06028940758";
        String location3 = "kitchen";
        executeActionLight(lightClient, id3, LightClient.CREATE_LIGHT, location3);
        
        String id4 = "1a8fe00b-a95c-4718-b039-e5b19a5e21c4";
        String location4 = "Labo";
        executeActionLight(lightClient, id4, LightClient.CREATE_LIGHT, location4);
        
        String id5 = "1a8bf00b-aa5c-4718-b039-e5b19a5e21c4";
        String location5 = "Labo 2";
        executeActionLight(lightClient, id5, LightClient.CREATE_LIGHT, location5);
        
        
        String id6 = "1bbbe00b-a95c-4718-b039-e5b19a5e21c4";
        String location6 = "test unit room 1";
        executeActionLight(lightClient, id6, LightClient.CREATE_LIGHT, location6);
        
        String id7 = "1ccce00b-a95c-4718-b039-e5b19a5e21c4";
        String location7 = "test unit room 2";
        executeActionLight(lightClient, id7, LightClient.CREATE_LIGHT, location7);
        
        String id8 = "1ddde00b-a95c-4718-b039-e5b19a5e21c4";
        String location8 = "test unit room 3";
        executeActionLight(lightClient, id8, LightClient.CREATE_LIGHT, location8);
        
        // We now switch on all lights.
        executeActionLight(lightClient, id2, LightClient.SWITCH_ON, null);
        executeActionLight(lightClient, id4, LightClient.SWITCH_ON, null);
        executeActionLight(lightClient, id6, LightClient.SWITCH_ON, null);
        executeActionLight(lightClient, id8, LightClient.SWITCH_ON, null);
        executeActionLight(lightClient, id1, LightClient.SWITCH_ON, null);
        executeActionLight(lightClient, id3, LightClient.SWITCH_ON, null);
        executeActionLight(lightClient, id5, LightClient.SWITCH_ON, null);
        executeActionLight(lightClient, id7, LightClient.SWITCH_ON, null);
        
        // Swith off some lights...
        executeActionLight(lightClient, id2, LightClient.SWITCH_OFF, null);
        executeActionLight(lightClient, id4, LightClient.SWITCH_OFF, null);
        executeActionLight(lightClient, id6, LightClient.SWITCH_OFF, null);
        executeActionLight(lightClient, id8, LightClient.SWITCH_OFF, null);
        
        // Now we update location only.
        executeActionLight(lightClient, id1, LightClient.UPDATE_LIGHT_LOCATION, "sleeping room 1");
        executeActionLight(lightClient, id3, LightClient.UPDATE_LIGHT_LOCATION, "sleeping room 2");
        executeActionLight(lightClient, id5, LightClient.UPDATE_LIGHT_LOCATION, "sleeping room 3");
        executeActionLight(lightClient, id7, LightClient.UPDATE_LIGHT_LOCATION, "sleeping room 4");
        
        // Switch off the rest and set the first to On.
        executeActionLight(lightClient, id3, LightClient.SWITCH_OFF, null);
        executeActionLight(lightClient, id5, LightClient.SWITCH_OFF, null);
        executeActionLight(lightClient, id7, LightClient.SWITCH_OFF, null);
        
        // Find the light state :
        executeActionLight(lightClient, id2, LightClient.RETRIEVE, null);
        
        
        
        // The end...
        // Delete some lights.
        executeActionLight(lightClient, id1, LightClient.DELETE_LIGHT, "sleeping room 1");
        executeActionLight(lightClient, id3, LightClient.DELETE_LIGHT, "sleeping room 2");
        executeActionLight(lightClient, id5, LightClient.DELETE_LIGHT, "sleeping room 3");
        executeActionLight(lightClient, id7, LightClient.DELETE_LIGHT, "sleeping room 4");
        
        
    }

    /**
     * Execute an action on light client.
     * @param client
     * @param id
     * @param action
     * @param location
     * @return 
     */
    private static String executeActionLight(final LightClient client, final String id, final String action, final String location) {
        String response;
        try {
            switch (action) {
                case LightClient.CREATE_LIGHT:
                    response = client.createLight(id, location);
                    break;
                case LightClient.DELETE_LIGHT:
                    response = client.deleteLight(id);
                    break;
                case LightClient.SWITCH_ON:
                    response = client.switchOn(id);
                    break;
                case LightClient.SWITCH_OFF:
                    response = client.switchOff(id);
                    break;
                case LightClient.UPDATE_LIGHT_LOCATION:
                    response = client.updateLightLocation(id, location);
                    break;
                case LightClient.RETRIEVE:
                    response = client.retrieve(id);
                    log.info("Retrieve a light : " + response);
                    break;
                default:
                    response = "This command " + action + " doesnt exist.";
                    break;

            }

        } catch (IOException | TimeoutException ex) {
            response = ex.getMessage();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        return response;
    }

}
