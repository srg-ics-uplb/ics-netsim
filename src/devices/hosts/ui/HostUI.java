package devices.hosts.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import platform.gui.MainFrame;
import devices.Device;
import devices.hosts.Host;
import devices.interfaces.Interface;
import devices.ui.DeviceUI;


public class HostUI extends DeviceUI implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6867805375146846925L;
	Host hostName;
    
    public HostUI(Host host) {
        super(host);
        hostName= host;
        setPopupMenu(popupMenu);
        initializePopupMenu();
        configure.addActionListener(this);
        console.addActionListener(this);
        ping.addActionListener(this);
        ports.addActionListener(this);
        portScan.addActionListener(this);
        requester.addActionListener(this);
        delete.addActionListener(this);
        properties.addActionListener(this);
        addConnection.addActionListener(this);
        removeConnection.addActionListener(this);
        

    }

    public void initializePopupMenu() {
        popupMenu.add(configure);
        popupMenu.add(console);
        popupMenu.add(ping);
        popupMenu.addSeparator();
        popupMenu.add(ports);
        popupMenu.add(portScan);
        popupMenu.add(requester);
        popupMenu.addSeparator();
        popupMenu.add(addConnection);
        popupMenu.add(removeConnection);
        popupMenu.addSeparator();
        popupMenu.add(delete);
        popupMenu.addSeparator();
        popupMenu.add(properties);
    }

    public void setAddInterfaces() {
        MainFrame.HOST_PORT_DIALOG.addOrRemove=1;
        MainFrame.HOST_PORT_DIALOG.interfaceCount=0;
    	addInterfaces = device.getOpenedInterfaces();
        if (addInterfaces.length >= 0){
        	MainFrame.HOST_PORT_DIALOG.interfaceCount=addInterfaces.length;
        	MainFrame.HOST_PORT_DIALOG.setInterfaceArray();
            for (int i = 0; i < addInterfaces.length; i++) {
            	MainFrame.HOST_PORT_DIALOG.setActiveHostInterface(addInterfaces[i], i);

            }
            MainFrame.HOST_PORT_DIALOG.setLights();
        } else MainFrame.HOST_PORT_DIALOG.interfaceCount=-1;
        
    }

    public void setRemoveInterfaces() {
    	MainFrame.HOST_PORT_DIALOG.addOrRemove=-1;
    	MainFrame.HOST_PORT_DIALOG.interfaceCount=0;
    	
        removeInterfaces = device.getClosedInterfaces();
        if (removeInterfaces.length >= 0){
	       	//System.out.println(removeInterfaces.length);
	        MainFrame.HOST_PORT_DIALOG.interfaceCount=removeInterfaces.length;
	        MainFrame.HOST_PORT_DIALOG.setInterfaceArray();
	        
	        for (int i = 0; i < removeInterfaces.length; i++) {
	        	MainFrame.HOST_PORT_DIALOG.setActiveHostInterface(removeInterfaces[i], i);
	        }
	        MainFrame.HOST_PORT_DIALOG.setLights();
        } else MainFrame.HOST_PORT_DIALOG.interfaceCount = -1;
        
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source.equals(configure)) {
            MainFrame.HOST_CONFIG_DIALOG.setHost((Host) getDevice());
            MainFrame.HOST_CONFIG_DIALOG.showCentered();
        } else if (source.equals(console)) {
            getDevice().getConsole().showCentered();
        } else if (source.equals(ping)) {
            MainFrame.HOST_PING_DIALOG.setHost((Host) getDevice());
            MainFrame.HOST_PING_DIALOG.showCentered();
        } else if (source.equals(ports)) {
            MainFrame.HOST_CONFIGURE_PORT_DIALOG.setHost((Host) getDevice());
            MainFrame.HOST_CONFIGURE_PORT_DIALOG.showCentered();
        } else if (source.equals(portScan)) {
            MainFrame.HOST_PORT_SCAN_DIALOG.setHost((Host) getDevice());
            MainFrame.HOST_PORT_SCAN_DIALOG.showCentered();
        }  else if (source.equals(requester)) {
            MainFrame.HOST_HTTP_REQUEST_DIALOG.setHost((Host) getDevice());
            MainFrame.HOST_HTTP_REQUEST_DIALOG.showCentered();
        }  else if (source.equals(properties)) {
            MainFrame.HOST_PROPERTY_DIALOG.setHost((Host) getDevice());
            MainFrame.HOST_PROPERTY_DIALOG.showCentered();
        } else if (source.equals(delete)) {
            Device device = getDevice();
            Interface[] interfaces = device.getClosedInterfaces();

            for (int i = 0; i < interfaces.length; i++) {
                MainFrame.DESIGNER_PANEL.removeConnection(interfaces[i]);
            }

            for (int i = 0; i < Device.DEVICES.size(); i++) {
                if (device == (Device) Device.DEVICES.get(i)) {
                    Device.DEVICES.remove(i);
                    MainFrame.DESIGNER_PANEL.repaint();

                    break;
                }
            }
        } else if (source.equals(addConnection)) {
        	MainFrame.HOST_PORT_DIALOG.hideAll();
        	this.setAddInterfaces();
        	MainFrame.HOST_PORT_DIALOG.showCentered();

        } else if (source.equals(removeConnection)) {
        	MainFrame.HOST_PORT_DIALOG.hideAll();
            this.setRemoveInterfaces();
        	MainFrame.HOST_PORT_DIALOG.showCentered();
        }
    }
}
