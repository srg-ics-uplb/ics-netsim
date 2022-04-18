package devices.interfaces;

import devices.Device;

import devices.addresses.IPAddress;
import devices.addresses.SubnetMask;

import devices.routers.accesslists.IPAccessList;

import devices.switches.ports.SwitchPort;

import java.io.Serializable;


public class Interface implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1204909967705265118L;
	public final static int DIRECTION_IN = 0;
    public final static int DIRECTION_OUT = 1;
    public final static String UP = "up";
    public final static String DOWN = "down";
    public final static String ADMINISTRATIVELY_DOWN = "administratively down";
    private String name;
    private Device device;
    private String consoleName;
    private Interface connectedInterface;
    private final IPAddress ipAddress = new IPAddress();
    private final SubnetMask subnetMask = new SubnetMask();
    private final IPAddress defaultGateway = new IPAddress();
    private String state = ADMINISTRATIVELY_DOWN;
    private String protocolStatus = DOWN;
    private SwitchPort switchPort;
    private IPAccessList IPAccessListIn;
    private IPAccessList IPAccessListOut;

    public Interface(Device device, String name, String consoleName) {
        this.device = device;
        this.name = name;
        this.consoleName = consoleName;
    }

    public void setIPAccessListIn(IPAccessList accessList) {
        this.IPAccessListIn = accessList;
    }

    public IPAccessList getIPAccessListIn() {
        return IPAccessListIn;
    }

    public void setIPAccessListOut(IPAccessList accessList) {
        this.IPAccessListOut = accessList;
    }

    public IPAccessList getIPAccessListOut() {
        return IPAccessListOut;
    }

    public void setSwitchPort(SwitchPort switchPort) {
        this.switchPort = switchPort;
    }

    public SwitchPort getSwitchPort() {
        return switchPort;
    }

    public void setIPAddress(int[] quartet) {
        ipAddress.setAddress(quartet[0], quartet[1], quartet[2], quartet[3]);
    }

    public void setSubnetMask(int[] quartet) {
        subnetMask.setMask(quartet[0], quartet[1], quartet[2], quartet[3]);
    }

    public void setDefaultGateway(int[] quartet) {
        defaultGateway.setAddress(quartet[0], quartet[1], quartet[2], quartet[3]);
    }

    public void setIPAddress(int a, int b, int c, int d) {
        ipAddress.setAddress(a, b, c, d);
    }

    public void setSubnetMask(int a, int b, int c, int d) {
        subnetMask.setMask(a, b, c, d);
    }

    public void setDefaultGateway(int a, int b, int c, int d) {
        defaultGateway.setAddress(a, b, c, d);
    }

    public Device getDevice() {
        return device;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public String getConsoleName() {
        return consoleName;
    }

    public void setConnectedInterface(Interface connectedInterface) {
        this.connectedInterface = connectedInterface;
    }

    public Interface getConnectedInterface() {
        return connectedInterface;
    }

    public boolean isOpened() {
        if (connectedInterface == null) {
            return true;
        }

        return false;
    }

    public IPAddress getIPAddress() {
        return ipAddress;
    }

    public SubnetMask getSubnetMask() {
        return subnetMask;
    }

    public IPAddress getDefaultGateway() {
        return defaultGateway;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setProtocolStatus(String status) {
        this.protocolStatus = status;
    }

    public String getProtocoStatus() {
        return protocolStatus;
    }

    public String getDescription() {
        String str = null;

        if (this instanceof Ethernet) {
            return ((Ethernet) this).getDescription();
        } else if (this instanceof Serial) {
            return ((Serial) this).getDescription();
        } else if (this instanceof FastEthernet) {
            return ((FastEthernet) this).getDescription();
        }

        return str;
    }
}
