package devices.routers.accesslists;

import java.util.Vector;

import devices.addresses.IPAddress;


public class IPAccessList extends AccessList {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -52792920207479806L;
	protected int index;
    protected final Vector permissions = new Vector();

    public void addIPPermission(IPPermission permission) {
        permissions.add(permission);
    }

    public IPPermission[] getIPPermissions() {
        IPPermission[] permissions = new IPPermission[this.permissions.size()];

        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = (IPPermission) this.permissions.get(i);
        }

        return permissions;
    }

    public int getIndex() {
        return index;
    }
    
    public static String getMaskedAddress(IPAddress address, Wildcard mask) {
        int[] addressQuartet = address.getAddress();
        int[] maskQuartet = mask.getMaskArray();
        StringBuffer maskedAddress = new StringBuffer();

        for (int i = 0; i < addressQuartet.length; i++) {
            maskedAddress.append(addressQuartet[i] & ~maskQuartet[i]);
            if (i != (addressQuartet.length - 1)) {
                maskedAddress.append(".");
            }
        }
        return maskedAddress.toString();
    }
}
