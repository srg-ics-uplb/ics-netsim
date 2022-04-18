package devices.ui;

import devices.Device;

import devices.hosts.ui.HostUI;
import devices.servers.ui.ServerUI;

import devices.interfaces.Interface;

import devices.routers.ui.RouterUI;

import devices.switches.ui.SwitchUI;

import java.io.Serializable;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class DeviceUI implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1402991619107746781L;
	public final static String ADD_CONNECTION = "ADD_CONNECTION";
    public final static String REMOVE_CONNECTION = "REMOVE_CONNECTION";
    protected Device device;
    protected Interface[] addInterfaces;
    protected Interface[] removeInterfaces;
    protected transient JPopupMenu popupMenu = new JPopupMenu();
    protected final JMenuItem configure = new JMenuItem("Configure");
    protected final JMenuItem console = new JMenuItem("Console");
    protected final JMenuItem ping = new JMenuItem("Ping");
    protected final JMenuItem requester = new JMenuItem("HTTP");
    protected final JMenuItem portScan = new JMenuItem("Port Scan");
    protected final JMenuItem addConnection = new JMenuItem("Add Connection");
    protected final JMenuItem removeConnection = new JMenuItem("Remove Connection");
    protected final JMenuItem delete = new JMenuItem("Delete");
    protected final JMenuItem ports = new JMenuItem("Ports");
    protected final JMenuItem properties = new JMenuItem("Properties");

    public DeviceUI(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setInterfaces() {
        if (this instanceof HostUI) {
            HostUI hostUI = (HostUI) this;

        } else if (this instanceof RouterUI) {
            RouterUI routerUI = (RouterUI) this;

        } else if (this instanceof SwitchUI) {
            SwitchUI switchUI = (SwitchUI) this;

        }
        else if (this instanceof ServerUI) {
            ServerUI serverUI = (ServerUI) this;

        }
    }

    public void initializePopupMenu() {
        if (this instanceof HostUI) {
            HostUI hostUI = (HostUI) this;
            hostUI.initializePopupMenu();
        } else if (this instanceof RouterUI) {
            RouterUI routerUI = (RouterUI) this;
            routerUI.initializePopupMenu();
        } else if (this instanceof SwitchUI) {
            SwitchUI switchUI = (SwitchUI) this;
            switchUI.initializePopupMenu();
        }
        else if (this instanceof ServerUI) {
            ServerUI serverUI = (ServerUI) this;
            serverUI.initializePopupMenu();
        }
    }
}
