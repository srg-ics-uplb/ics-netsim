package devices;

import devices.consoles.Console;

import devices.hosts.Host;

import devices.interfaces.Interface;

import devices.routers.Router;

import devices.servers.Server;
import devices.switches.Switch;

import devices.ui.DeviceUI;

import java.awt.Image;
import java.awt.Point;

import java.io.Serializable;

import java.lang.Math;
import java.util.Vector;

import javax.swing.JComponent;



public class Device implements Serializable {
	private static final long serialVersionUID = 4228969278096393190L;
    public static int deviceCount;
    public final static Vector DEVICES = new Vector();
    public final static String ROUTER2600 = "Router2600";
    public final static String SWITCH1900 = "Switch1900";
    public final static String SWITCH2950 = "Switch2950";
    public final static String SWITCH3550 = "Switch3550";
    public final static String HOST = "Host";
    public final static String SERVER = "Server";
    private String name;
    public String macAddress;
    private Point location;
    public transient Image image;
    private Interface[] interfaces;
    private DeviceUI deviceUI;
    private transient Console console;

    public char randomHexChar(){
    	int tempInt;
    	char returnChar = '0';
    	tempInt = (int) (16 * Math.random());
    	switch(tempInt){
    		case 0:		returnChar = '0'; break;
    		case 1:		returnChar = '1'; break;
    		case 2:		returnChar = '2'; break;
    		case 3:		returnChar = '3'; break;
    		case 4:		returnChar = '4'; break;
    		case 5:		returnChar = '5'; break;
    		case 6:		returnChar = '6'; break;
    		case 7:		returnChar = '7'; break;
    		case 8:		returnChar = '8'; break;
    		case 9:		returnChar = '9'; break;
    		case 10:	returnChar = 'A'; break;
    		case 11:	returnChar = 'B'; break;
    		case 12:	returnChar = 'C'; break;
    		case 13:	returnChar = 'D'; break;
    		case 14:	returnChar = 'E'; break;
    		case 15:	returnChar = 'F'; break;
    		case 16:	returnChar = 'F'; break;
    	}
    	
    	return returnChar;
    	
    }

    public Device(String name, Point location) {
        this.name = name;
        this.location = location;
        deviceCount++;
        DEVICES.add(this);
    }

    public static void resetDeviceCount() {
        deviceCount = 0;
        Router.routerCount = 0;
        Switch.switchCount = 0;
        Host.hostCount = 0;
        Server.serverCount = 0;
    }

    public static void load(Device[] devices) {
        DEVICES.removeAllElements();
        resetDeviceCount();

        for (int i = 0; i < devices.length; i++) {
            DEVICES.add(devices[i]);
            deviceCount++;

            if (devices[i] instanceof Router) {
                Router.routerCount++;
            } else if (devices[i] instanceof Switch) {
                Switch.switchCount++;
            } else if (devices[i] instanceof Host) {
                Host.hostCount++;
            } else if (devices[i] instanceof Server) {
                Server.serverCount++;
            }
        }
    }

    public static Device[] getDevices() {
        Device[] devices = new Device[DEVICES.size()];

        for (int i = 0; i < DEVICES.size(); i++) {
            devices[i] = (Device) DEVICES.get(i);
        }

        return devices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public void setInterfaces(Interface[] interfaces) {
        this.interfaces = interfaces;
    }

    public Interface[] getInterfaces() {
        return interfaces;
    }

    public void setDeviceUI(DeviceUI deviceUI) {
        this.deviceUI = deviceUI;
    }

    public DeviceUI getDeviceUI() {
        return deviceUI;
    }

    public String toString() {
        return name;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public Console getConsole() {
        return console;
    }
    
    public void setMacAddress(String macAddress){
    	this.macAddress = macAddress;
    }
    
    public String getMacAddress(){
    	return macAddress;
    }

    public Interface[] getOpenedInterfaces() {
        Vector openedInterfaces = new Vector();

        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].isOpened()) {
                openedInterfaces.add(interfaces[i]);
            }
        }

        Interface[] copy = new Interface[openedInterfaces.size()];
        openedInterfaces.copyInto(copy);

        return copy;
    }

    public Interface[] getClosedInterfaces() {
        Vector closedInterfaces = new Vector();

        for (int i = 0; i < interfaces.length; i++) {
            if (!interfaces[i].isOpened()) {
                closedInterfaces.add(interfaces[i]);
            }
        }

        Interface[] copy = new Interface[closedInterfaces.size()];
        closedInterfaces.copyInto(copy);

        return copy;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public boolean contains(Point point, Device device, JComponent observer) {
        Point location = getLocation();

        if ((location.getX() <= point.getX()) && (point.getX() <= (location.getX() + image.getWidth(observer))) && (location.getY() <= point.getY()) && (point.getY() <= (location.getY() + image.getHeight(observer)))) {
            return true;
        }

        return false;
    }
}
