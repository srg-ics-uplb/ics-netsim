package devices.routers.routingprotocols;

import java.io.Serializable;

public abstract class RoutingProtocol implements Serializable{
    public static int ADMINISTRATIVE_DISTANCE;
    public static int UPDATE_INTERVAL;

    public int getAdministrativeDistance() {
        return ADMINISTRATIVE_DISTANCE;
    }

    public int getUpdateInterval() {
        return UPDATE_INTERVAL;
    }
}
