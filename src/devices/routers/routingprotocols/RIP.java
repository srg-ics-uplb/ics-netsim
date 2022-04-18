package devices.routers.routingprotocols;

public class RIP extends RoutingProtocol {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1033988470566772059L;

	public RIP() {
        super();
        ADMINISTRATIVE_DISTANCE = 120;
        UPDATE_INTERVAL = 30000;
    }
}
