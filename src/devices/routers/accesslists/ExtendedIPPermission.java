package devices.routers.accesslists;

import devices.addresses.IPAddress;

public class ExtendedIPPermission extends IPPermission {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3002029920853835565L;
	public final static int DENY = 0;
    public final static int PERMIT = 1;
    public final static int HOSTNAME = 0;
    public final static int IP_ADDRESS = 1;
    public final static int EIGRP = 0;
    public final static int ICMP = 1;
    public final static int IGRP = 2;
    public final static int IP = 3;
    public final static int OSPF = 4;
    public final static int TCP = 5;
    public final static int UDP = 6;
    private final IPAddress source;
    private final IPAddress destination;
    private final Wildcard sourceWildcard;
    private final Wildcard destinationWildcard;
    private final int permission;
    private final int protocol;
    private int portNumber = -1;

    public ExtendedIPPermission(int permission, int protocol, String source, String sourceWildcard, String destination, String destinationWildcard) {
        this.permission = permission;
        this.source = new IPAddress(source);
        this.sourceWildcard = new Wildcard(sourceWildcard);
        this.destination = new IPAddress(destination);
        this.destinationWildcard = new Wildcard(destinationWildcard);        
        this.protocol = protocol;
    }
    
    public ExtendedIPPermission(int permission, int protocol, String source, String sourceWildcard, String destination, String destinationWildcard, int portNumber) {
        this.permission = permission;
        this.source = new IPAddress(source);
        this.sourceWildcard = new Wildcard(sourceWildcard);
        this.destination = new IPAddress(destination);
        this.destinationWildcard = new Wildcard(destinationWildcard);        
        this.protocol = protocol;
        this.portNumber = portNumber;
    }

    public IPAddress getSource() {
        return source;
    }
    
    public IPAddress getDestination(){
    	return destination;
    }

    public int getPermission() {
        return permission;
    }

    public Wildcard getSourceWildcard() {
        return sourceWildcard;
    }
    
    public Wildcard getDestinationWildcard(){
    	return destinationWildcard;
    }

    public int getProtocol() {
        return protocol;
    }
    
    public int getPortNumber(){
    	return portNumber;
    }
}
