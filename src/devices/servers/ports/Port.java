package devices.servers.ports;

import java.io.Serializable;


public class Port implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4464588403710411642L;
    public int portNumber;
    public String portName;
    public String portDescription;
    public boolean portOpened;

    public Port() {
        this.portOpened = true;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortDescription(String portDescription) {
        this.portDescription = portDescription;
    }

    public String getPortDescription() {
        return portDescription;
    }

    public void setPortOpened(boolean portOpened) {
        this.portOpened = portOpened;
    }

    public boolean getPortStatus() {
        return portOpened;
    }

    public boolean isOpened() {
        return portOpened;
    }
}
