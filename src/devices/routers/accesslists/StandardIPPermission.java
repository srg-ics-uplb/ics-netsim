package devices.routers.accesslists;

import devices.addresses.IPAddress;

public class StandardIPPermission extends IPPermission {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6485087816013242186L;
	public final static int HOSTNAME = 0;
    public final static int IP_ADDRESS = 1;
    private final IPAddress address;
    private final Wildcard wildcard;
    private final int permission;

    public StandardIPPermission(int permission, String address, String wildcard) {
        this.permission = permission;
        this.address = new IPAddress(address);
        this.wildcard = new Wildcard(wildcard);
    }

    public IPAddress getAddress() {
        return address;
    }

    public int getPermission() {
        return permission;
    }

    public Wildcard getWildcard() {
        return wildcard;
    }
}
