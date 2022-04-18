package devices.switches.ports;

import devices.interfaces.Interface;

import devices.switches.vlan.VLAN;

import java.io.Serializable;


public class SwitchPort implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4683331975203081804L;
	private final Interface portInterface;
    private final String portNumber;
    private VLAN vlan;

    public SwitchPort(Interface portInterface, String portNumber) {
        this.portInterface = portInterface;
        this.portNumber = portNumber;
        portInterface.setSwitchPort(this);
    }

    public Interface getPortInterface() {
        return portInterface;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setVLAN(VLAN vlan) {
        this.vlan = vlan;
    }

    public VLAN getVLAN() {
        return vlan;
    }
}
