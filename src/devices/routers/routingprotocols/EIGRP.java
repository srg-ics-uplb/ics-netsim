package devices.routers.routingprotocols;

public class EIGRP extends RoutingProtocol {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3253331451727424320L;

	public EIGRP() {
        super();
        ADMINISTRATIVE_DISTANCE = 90;
        UPDATE_INTERVAL = 90000;
    }
}
