package devices.routers.routingprotocols;

public class IGRP extends RoutingProtocol {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8828483885817066826L;
	private int ASNumber;

    public IGRP() {
        super();
        ADMINISTRATIVE_DISTANCE = 100;
        UPDATE_INTERVAL = 90000;
    }

    public void setASNumber(int ASNumber) {
        this.ASNumber = ASNumber;
    }

    public int getASNumber() {
        return ASNumber;
    }
}
