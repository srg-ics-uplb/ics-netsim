package devices.switches;

import java.awt.Point;
import java.util.Vector;

import devices.Device;
import devices.addresses.IPAddress;
import devices.addresses.SubnetMask;
import devices.interfaces.Interface;
import devices.switches.ports.SwitchPort;
import devices.switches.vlan.VLAN;


public class Switch extends Device {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8502739950309450299L;
	public static int switchCount;
    private String password = "";
    private String secret = "";
    private final IPAddress ipAddress = new IPAddress();
    private final SubnetMask subnetMask = new SubnetMask();
    private String defaultGateway = "";
    private final Vector VLANS = new Vector();
    private SwitchPort[] ports;

    public Switch(String name, Point location) {
        super(name, location);
        macAddress = "3000." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar();
        switchCount++;
    }

    public void setPorts(SwitchPort[] ports) {
        this.ports = ports;

        VLAN vlan1 = new VLAN(1);

        for (int i = 0; i < ports.length; i++) {
            ports[i].setVLAN(vlan1);
        }

        VLANS.add(vlan1);
    }

    public SwitchPort[] getPorts() {
        return ports;
    }
    
    public void setInterfaces(Interface[] interfaces){
    	for (int i = 0; i < interfaces.length; i++){
    		interfaces[i].setState(Interface.UP);
    	}
    	super.setInterfaces(interfaces);
    }

    public void addVLAN(VLAN vlan) {
        VLANS.add(vlan);
    }

    public VLAN getVLAN(int index) {
        VLAN returnVLAN = null;

        if ((index >= 0) || (index < VLANS.size())) {
            returnVLAN = (VLAN) VLANS.get(index);
        }

        return returnVLAN;
    }

    public VLAN[] getVLANS() {
        VLAN[] vlans = new VLAN[VLANS.size()];

        for (int i = 0; i < vlans.length; i++) {
            vlans[i] = (VLAN) VLANS.get(i);
        }

        return vlans;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPassword() {
        return password;
    }

    public void setIPAddress(int[] quartet) {
        ipAddress.setAddress(quartet[0], quartet[1], quartet[2], quartet[3]);
    }

    public void setSubnetMask(int[] quartet) {
        ipAddress.setAddress(quartet[0], quartet[1], quartet[2], quartet[3]);
    }

    public void setIPAddress(int a, int b, int c, int d) {
        ipAddress.setAddress(a, b, c, d);
    }

    public void setSubnetMask(int a, int b, int c, int d) {
        subnetMask.setMask(a, b, c, d);
    }

    public IPAddress getIPAddress() {
        return ipAddress;
    }

    public void setDefaultGateway(String defaultGateway) {
        this.defaultGateway = defaultGateway;
    }

    public String getDefaultGateway() {
        return defaultGateway;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isPasswordEnabled() {
        if (password.equals("")) {
            return false;
        }

        return true;
    }

    public boolean isSecretEnabled() {
        if (secret.equals("")) {
            return false;
        }

        return true;
    }
}
