# ClientLightDemo
Client Light Demo API for occiware tutorial project

## Build : 
```mvn clean install```

## Execute : 
```java -cp ./target/ClientLightDemo-1.0-SNAPSHOT-jar-with-dependencies.jar org.occiware.light.ClientTestMain```

## Usage on Erocci-dbus-java :
This client is used to exchange message between Erocci-dbus-java and the light server demo application (javaFX) throught a websocket layer.

Actions supported by the api :
```create``` - Create a new light - ```create(String id,String location)``` <br/>
```delete``` - Delete an existing light - ```delete(String id)``` <br/>
```updateLocation``` - Update a light location - ```updateLocation(String id, String location)``` <br/>
```switchOff``` - Turn off the light - ```switchOff(String id)``` <br/>
```switchOn```  - Turn on the light  - ```switchOn(String id)```  <br/>

The class used for connecting to server and referenced these actions is org.```occiware.light.LightClient.java``` <br/>
